package com.memoreel.common.enums;

/**
 * Enum representing the status of a project in the Memoreel system.
 * 
 * Status Flow:
 * draft → processing → completed → published
 *            ↓
 *         failed
 * 
 * @author Memoreel Team
 */
public enum ProjectStatus {
    /**
     * Project is in draft state and can be edited
     */
    DRAFT,
    
    /**
     * Video generation is in progress, project is locked for modification
     */
    PROCESSING,
    
    /**
     * Video generation completed successfully, ready for preview and publish
     */
    COMPLETED,
    
    /**
     * Project has been published and is publicly accessible
     */
    PUBLISHED,
    
    /**
     * Video generation failed, can be retried
     */
    FAILED
}

// Made with Bob
