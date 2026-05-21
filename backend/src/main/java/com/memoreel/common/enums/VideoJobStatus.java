package com.memoreel.common.enums;

/**
 * Enum representing the status of a video generation job.
 * 
 * @author Memoreel Team
 */
public enum VideoJobStatus {
    /**
     * Job is queued and waiting to be processed
     */
    QUEUED,
    
    /**
     * Job is currently being processed
     */
    PROCESSING,
    
    /**
     * Job completed successfully
     */
    COMPLETED,
    
    /**
     * Job failed during processing
     */
    FAILED
}

// Made with Bob
