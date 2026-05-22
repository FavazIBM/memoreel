package com.memoreel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for Memoreel.
 * 
 * Memoreel is a memory reel sharing platform that enables users to create
 * and share emotionally rich memory video reels for special occasions and memorials.
 * 
 * @author Memoreel Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class MemoreelApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoreelApplication.class, args);
    }
}

// Made with Bob
