package com.memoreel.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT configuration properties
 * Loads JWT settings from application properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    
    /**
     * Secret key for signing JWT tokens
     */
    private String secret;
    
    /**
     * Access token expiration time in milliseconds (default: 24 hours)
     */
    private long expiration = 86400000L; // 24 hours
    
    /**
     * Refresh token expiration time in milliseconds (default: 7 days)
     */
    private long refreshExpiration = 604800000L; // 7 days
}

// Made with Bob
