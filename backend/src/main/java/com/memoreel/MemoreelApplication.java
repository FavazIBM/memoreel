package com.memoreel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Memoreel Backend
 * 
 * Memoreel is a memory reel sharing platform that enables users to create
 * emotionally rich video reels for special occasions and memorials.
 * 
 * @author Memoreel Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class MemoreelApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoreelApplication.class, args);
    }
}

// Made with Bob
