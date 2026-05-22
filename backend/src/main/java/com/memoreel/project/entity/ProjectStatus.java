package com.memoreel.project.entity;

/**
 * Enum representing the status of a project in its lifecycle.
 * 
 * This enum follows a strict state machine pattern:
 * DRAFT -> PROCESSING -> COMPLETED -> PUBLISHED
 *                     -> FAILED
 * 
 * State transitions must be validated to ensure data integrity.
 */
public enum ProjectStatus {
    /**
     * Initial state - project is being created, media is being uploaded
     */
    DRAFT,
    
    /**
     * Video generation is in progress
     */
    PROCESSING,
    
    /**
     * Video has been successfully generated and is ready for preview
     */
    COMPLETED,
    
    /**
     * Project has been published and is publicly accessible
     */
    PUBLISHED,
    
    /**
     * Video generation failed
     */
    FAILED;
    
    /**
     * Check if transition to target status is valid.
     * 
     * @param target the target status to transition to
     * @return true if transition is valid, false otherwise
     */
    public boolean canTransitionTo(ProjectStatus target) {
        return switch (this) {
            case DRAFT -> target == PROCESSING;
            case PROCESSING -> target == COMPLETED || target == FAILED;
            case COMPLETED -> target == PUBLISHED || target == PROCESSING;
            case FAILED -> target == PROCESSING;
            case PUBLISHED -> false; // Published projects cannot change status
        };
    }
}

// Made with Bob
