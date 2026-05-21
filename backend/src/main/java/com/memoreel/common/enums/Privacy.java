package com.memoreel.common.enums;

/**
 * Enum representing privacy settings for published projects.
 * 
 * Phase 1 Note: Only PUBLIC is fully implemented.
 * FRIENDS and PRIVATE are reserved for future implementation.
 * 
 * @author Memoreel Team
 */
public enum Privacy {
    /**
     * Project is accessible to anyone with the link (Phase 1 default)
     */
    PUBLIC,
    
    /**
     * Project is accessible only to friends (Future implementation)
     */
    FRIENDS,
    
    /**
     * Project is accessible only to the owner (Future implementation)
     */
    PRIVATE
}

// Made with Bob
