package com.memoreel.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Configuration for Spring Batch and async task execution.
 * Configures thread pool for video processing jobs and batch job infrastructure.
 */
@Configuration
@EnableBatchProcessing
@EnableAsync
@EnableScheduling
public class BatchConfig {
    
    /**
     * Configure task executor for async video processing.
     * Uses a thread pool to handle multiple video generation jobs concurrently.
     */
    @Bean(name = "videoProcessingTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Core pool size: minimum number of threads to keep alive
        executor.setCorePoolSize(5);
        
        // Max pool size: maximum number of threads
        executor.setMaxPoolSize(10);
        
        // Queue capacity: number of tasks to queue before rejecting
        executor.setQueueCapacity(25);
        
        // Thread name prefix for easier debugging
        executor.setThreadNamePrefix("video-processing-");
        
        // Wait for tasks to complete on shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // Timeout for waiting on shutdown (30 seconds)
        executor.setAwaitTerminationSeconds(30);
        
        // Rejection policy: caller runs the task if queue is full
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        
        executor.initialize();
        
        return executor;
    }
    
    /**
     * Configure Spring Batch JobRepository.
     * Stores job execution metadata in the database.
     */
    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) 
            throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(1000);
        factory.afterPropertiesSet();
        return factory.getObject();
    }
    
    /**
     * Configure JobLauncher for launching batch jobs.
     */
    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository, TaskExecutor taskExecutor) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
    
    /**
     * Configure transaction manager for batch operations.
     */
    @Bean
    public PlatformTransactionManager batchTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

// Made with Bob
