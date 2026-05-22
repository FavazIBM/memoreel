package com.memoreel.video.service;

import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.project.entity.OccasionType;
import com.memoreel.video.entity.Template;
import com.memoreel.video.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing video templates.
 * Provides deterministic template selection based on occasion and mood.
 */
@Service
@Transactional(readOnly = true)
public class TemplateService {
    
    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);
    
    private final TemplateRepository templateRepository;
    
    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }
    
    /**
     * Select a template based on occasion type and mood.
     * Uses deterministic fallback logic:
     * 1. Try to find template with exact occasion + mood match
     * 2. Fall back to first template for the occasion
     * 3. Fall back to default template (BIRTHDAY with vibrant mood)
     */
    public Template selectTemplate(OccasionType occasionType, String mood) {
        logger.info("Selecting template for occasion: {}, mood: {}", occasionType, mood);
        
        // Try exact match first
        if (mood != null && !mood.isEmpty()) {
            Template template = templateRepository.findByOccasionTypeAndMood(occasionType, mood)
                .orElse(null);
            
            if (template != null) {
                logger.info("Found exact match template: {}", template.getName());
                return template;
            }
        }
        
        // Fall back to first template for occasion
        Template occasionTemplate = templateRepository.findFirstByOccasionType(occasionType)
            .orElse(null);
        
        if (occasionTemplate != null) {
            logger.info("Using occasion default template: {}", occasionTemplate.getName());
            return occasionTemplate;
        }
        
        // Fall back to default template
        logger.warn("No template found for occasion: {}, using default", occasionType);
        return getDefaultTemplate(occasionType);
    }
    
    /**
     * Get the default template for an occasion type.
     * If no template exists for the occasion, returns a generic default.
     */
    public Template getDefaultTemplate(OccasionType occasionType) {
        return templateRepository.findFirstByOccasionType(occasionType)
            .orElseGet(() -> {
                // Ultimate fallback: get any template
                logger.warn("No default template for {}, using first available template", occasionType);
                return templateRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "No templates available in the system. Please add templates to the database."));
            });
    }
    
    /**
     * Get all available templates.
     */
    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }
    
    /**
     * Get all templates for a specific occasion type.
     */
    public List<Template> getTemplatesByOccasion(OccasionType occasionType) {
        return templateRepository.findByOccasionType(occasionType);
    }
    
    /**
     * Get a template by ID.
     */
    public Template getTemplateById(Long id) {
        return templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));
    }
    
    /**
     * Create a new template.
     */
    @Transactional
    public Template createTemplate(Template template) {
        logger.info("Creating new template: {}", template.getName());
        return templateRepository.save(template);
    }
    
    /**
     * Update an existing template.
     */
    @Transactional
    public Template updateTemplate(Long id, Template templateDetails) {
        Template template = getTemplateById(id);
        
        template.setName(templateDetails.getName());
        template.setOccasionType(templateDetails.getOccasionType());
        template.setMood(templateDetails.getMood());
        template.setDurationPerImage(templateDetails.getDurationPerImage());
        template.setTransitionType(templateDetails.getTransitionType());
        template.setMusicTrackUrl(templateDetails.getMusicTrackUrl());
        template.setTextStyle(templateDetails.getTextStyle());
        template.setConfig(templateDetails.getConfig());
        
        logger.info("Updated template: {}", template.getName());
        return templateRepository.save(template);
    }
    
    /**
     * Delete a template.
     */
    @Transactional
    public void deleteTemplate(Long id) {
        Template template = getTemplateById(id);
        logger.info("Deleting template: {}", template.getName());
        templateRepository.delete(template);
    }
}

// Made with Bob
