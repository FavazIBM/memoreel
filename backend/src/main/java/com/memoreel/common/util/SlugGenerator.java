package com.memoreel.common.util;

import com.memoreel.public_link.repository.PublicLinkRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SlugGenerator {
    
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 12;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    private final PublicLinkRepository publicLinkRepository;
    
    public SlugGenerator(PublicLinkRepository publicLinkRepository) {
        this.publicLinkRepository = publicLinkRepository;
    }
    
    /**
     * Generate a unique slug for public links
     * @return A unique alphanumeric slug
     */
    public String generateSlug() {
        String slug;
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            slug = generateRandomSlug();
            attempts++;
            
            if (attempts >= maxAttempts) {
                // If we can't find a unique slug after max attempts, increase length
                slug = generateRandomSlug(MAX_LENGTH + 2);
            }
        } while (!isUnique(slug));
        
        return slug;
    }
    
    /**
     * Generate a random slug with random length between MIN_LENGTH and MAX_LENGTH
     */
    private String generateRandomSlug() {
        int length = MIN_LENGTH + RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
        return generateRandomSlug(length);
    }
    
    /**
     * Generate a random slug with specified length
     */
    private String generateRandomSlug(int length) {
        StringBuilder slug = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            slug.append(CHARACTERS.charAt(index));
        }
        
        return slug.toString();
    }
    
    /**
     * Check if a slug is unique in the database
     */
    public boolean isUnique(String slug) {
        return !publicLinkRepository.existsBySlug(slug);
    }
}

// Made with Bob
