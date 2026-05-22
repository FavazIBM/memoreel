package com.memoreel.video.entity;

/**
 * Enum representing the status of a video generation job.
 * 
 * Tracks the lifecycle of asynchronous video processing jobs.
 */
public enum VideoJobStatus {
    /**
     * Job is queued and waiting to be processed
     */
    PENDING,
    
    /**
     * Job is currently being processed
     */
    PROCESSING,
    
    /**
     * Job completed successfully
     */
    COMPLETED,
    
    /**
     * Job failed due to an error
     */
    FAILED
}

// Made with Bob
