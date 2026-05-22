package com.memoreel.video.repository;

import com.memoreel.project.entity.OccasionType;
import com.memoreel.video.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Template entity operations.
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    
    /**
     * Find all templates for a specific occasion type.
     */
    List<Template> findByOccasionType(OccasionType occasionType);
    
    /**
     * Find a template by occasion type and mood.
     */
    Optional<Template> findByOccasionTypeAndMood(OccasionType occasionType, String mood);
    
    /**
     * Find the first template by occasion type (for default fallback).
     */
    Optional<Template> findFirstByOccasionType(OccasionType occasionType);
}

// Made with Bob
