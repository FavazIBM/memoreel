package com.memoreel.video.batch;

import com.memoreel.project.entity.ProjectStatus;
import com.memoreel.project.repository.ProjectRepository;
import com.memoreel.video.entity.VideoJob;
import com.memoreel.video.entity.VideoJobStatus;
import com.memoreel.video.repository.VideoJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled tasks for video job maintenance and cleanup.
 * Handles stale job detection and cleanup of old failed jobs.
 */
@Component
public class VideoJobScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoJobScheduler.class);
    
    private static final int STALE_JOB_TIMEOUT_MINUTES = 30;
    private static final int FAILED_JOB_RETENTION_DAYS = 7;
    
    private final VideoJobRepository videoJobRepository;
    private final ProjectRepository projectRepository;
    
    public VideoJobScheduler(
            VideoJobRepository videoJobRepository,
            ProjectRepository projectRepository
    ) {
        this.videoJobRepository = videoJobRepository;
        this.projectRepository = projectRepository;
    }
    
    /**
     * Clean up stale jobs that are stuck in PROCESSING status.
     * Runs every 5 minutes.
     */
    @Scheduled(fixedDelay = 300000) // 5 minutes = 300,000 ms
    @Transactional
    public void cleanupStaleJobs() {
        logger.info("Starting stale job cleanup");
        
        try {
            // Calculate cutoff time (30 minutes ago)
            LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(STALE_JOB_TIMEOUT_MINUTES);
            
            // Find jobs stuck in PROCESSING status
            List<VideoJob> staleJobs = videoJobRepository.findByStatusAndStartedAtBefore(
                VideoJobStatus.PROCESSING,
                cutoffTime
            );
            
            if (staleJobs.isEmpty()) {
                logger.info("No stale jobs found");
                return;
            }
            
            logger.warn("Found {} stale jobs", staleJobs.size());
            
            for (VideoJob job : staleJobs) {
                try {
                    logger.warn("Marking job {} as FAILED (stuck in PROCESSING for > {} minutes)", 
                        job.getId(), STALE_JOB_TIMEOUT_MINUTES);
                    
                    // Update job status to FAILED
                    job.setStatus(VideoJobStatus.FAILED);
                    job.setErrorMessage("Job timed out - stuck in PROCESSING for more than " + 
                        STALE_JOB_TIMEOUT_MINUTES + " minutes");
                    job.setCompletedAt(LocalDateTime.now());
                    videoJobRepository.save(job);
                    
                    // Update associated project status to FAILED
                    projectRepository.findById(job.getProjectId()).ifPresent(project -> {
                        if (project.getStatus() == ProjectStatus.PROCESSING) {
                            logger.warn("Marking project {} as FAILED due to stale job", project.getId());
                            project.setStatus(ProjectStatus.FAILED);
                            projectRepository.save(project);
                        }
                    });
                    
                } catch (Exception e) {
                    logger.error("Error processing stale job {}", job.getId(), e);
                }
            }
            
            logger.info("Stale job cleanup completed. Processed {} jobs", staleJobs.size());
            
        } catch (Exception e) {
            logger.error("Error during stale job cleanup", e);
        }
    }
    
    /**
     * Clean up old failed jobs.
     * Runs daily at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * *") // Daily at 2 AM
    @Transactional
    public void cleanupOldFailedJobs() {
        logger.info("Starting old failed job cleanup");
        
        try {
            // Calculate cutoff date (7 days ago)
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(FAILED_JOB_RETENTION_DAYS);
            
            // Find old failed jobs
            List<VideoJob> oldFailedJobs = videoJobRepository.findByStatusAndCompletedAtBefore(
                VideoJobStatus.FAILED,
                cutoffDate
            );
            
            if (oldFailedJobs.isEmpty()) {
                logger.info("No old failed jobs to clean up");
                return;
            }
            
            logger.info("Found {} old failed jobs to delete", oldFailedJobs.size());
            
            // Delete old failed jobs
            videoJobRepository.deleteAll(oldFailedJobs);
            
            logger.info("Old failed job cleanup completed. Deleted {} jobs", oldFailedJobs.size());
            
        } catch (Exception e) {
            logger.error("Error during old failed job cleanup", e);
        }
    }
    
    /**
     * Clean up orphaned PENDING jobs (jobs without associated projects).
     * Runs daily at 3 AM.
     */
    @Scheduled(cron = "0 0 3 * * *") // Daily at 3 AM
    @Transactional
    public void cleanupOrphanedJobs() {
        logger.info("Starting orphaned job cleanup");
        
        try {
            // Find all PENDING jobs
            List<VideoJob> pendingJobs = videoJobRepository.findByStatus(VideoJobStatus.PENDING);
            
            int orphanedCount = 0;
            
            for (VideoJob job : pendingJobs) {
                // Check if associated project exists
                boolean projectExists = projectRepository.existsById(job.getProjectId());
                
                if (!projectExists) {
                    logger.warn("Found orphaned job {} (project {} does not exist)", 
                        job.getId(), job.getProjectId());
                    
                    // Mark as FAILED
                    job.setStatus(VideoJobStatus.FAILED);
                    job.setErrorMessage("Associated project no longer exists");
                    job.setCompletedAt(LocalDateTime.now());
                    videoJobRepository.save(job);
                    
                    orphanedCount++;
                }
            }
            
            if (orphanedCount > 0) {
                logger.info("Orphaned job cleanup completed. Processed {} jobs", orphanedCount);
            } else {
                logger.info("No orphaned jobs found");
            }
            
        } catch (Exception e) {
            logger.error("Error during orphaned job cleanup", e);
        }
    }
    
    /**
     * Log job statistics.
     * Runs every hour.
     */
    @Scheduled(fixedDelay = 3600000) // 1 hour = 3,600,000 ms
    public void logJobStatistics() {
        try {
            long pendingCount = videoJobRepository.countByStatus(VideoJobStatus.PENDING);
            long processingCount = videoJobRepository.countByStatus(VideoJobStatus.PROCESSING);
            long completedCount = videoJobRepository.countByStatus(VideoJobStatus.COMPLETED);
            long failedCount = videoJobRepository.countByStatus(VideoJobStatus.FAILED);
            
            logger.info("Video Job Statistics - PENDING: {}, PROCESSING: {}, COMPLETED: {}, FAILED: {}",
                pendingCount, processingCount, completedCount, failedCount);
            
            // Warn if too many jobs are stuck in PROCESSING
            if (processingCount > 10) {
                logger.warn("High number of jobs in PROCESSING status: {}. Check for issues.", processingCount);
            }
            
            // Warn if too many jobs are PENDING
            if (pendingCount > 50) {
                logger.warn("High number of jobs in PENDING status: {}. Check processing capacity.", pendingCount);
            }
            
        } catch (Exception e) {
            logger.error("Error logging job statistics", e);
        }
    }
}

// Made with Bob
