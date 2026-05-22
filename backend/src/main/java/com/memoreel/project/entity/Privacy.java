package com.memoreel.project.entity;

/**
 * Enum representing privacy settings for published projects.
 * 
 * Controls who can view a published memory reel.
 */
public enum Privacy {
    /**
     * Anyone with the link can view the project
     */
    PUBLIC,
    
    /**
     * Only friends can view the project (future implementation)
     */
    FRIENDS,
    
    /**
     * Only the owner can view the project
     */
    PRIVATE
}

// Made with Bob
