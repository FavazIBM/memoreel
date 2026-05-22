package com.memoreel.video.entity;

import com.memoreel.project.entity.OccasionType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a video template with styling and configuration.
 */
@Entity
@Table(name = "templates")
public class Template {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "occasion_type", nullable = false)
    private OccasionType occasionType;
    
    @Column(nullable = false, length = 50)
    private String mood; // "vibrant", "calm", "elegant", "playful", "romantic"
    
    @Column(name = "duration_per_image", nullable = false)
    private Integer durationPerImage = 3; // seconds
    
    @Column(name = "transition_type", nullable = false, length = 50)
    private String transitionType = "fade"; // "fade", "slide", "zoom"
    
    @Column(name = "music_track_url", columnDefinition = "TEXT")
    private String musicTrackUrl; // S3 URL to background music
    
    @Column(name = "text_style", columnDefinition = "TEXT")
    private String textStyle; // JSON config for text overlays
    
    @Column(columnDefinition = "jsonb")
    private String config; // Additional template configuration
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    
    public Template() {
    }
    
    public Template(String name, OccasionType occasionType, String mood) {
        this.name = name;
        this.occasionType = occasionType;
        this.mood = mood;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public OccasionType getOccasionType() {
        return occasionType;
    }
    
    public void setOccasionType(OccasionType occasionType) {
        this.occasionType = occasionType;
    }
    
    public String getMood() {
        return mood;
    }
    
    public void setMood(String mood) {
        this.mood = mood;
    }
    
    public Integer getDurationPerImage() {
        return durationPerImage;
    }
    
    public void setDurationPerImage(Integer durationPerImage) {
        this.durationPerImage = durationPerImage;
    }
    
    public String getTransitionType() {
        return transitionType;
    }
    
    public void setTransitionType(String transitionType) {
        this.transitionType = transitionType;
    }
    
    public String getMusicTrackUrl() {
        return musicTrackUrl;
    }
    
    public void setMusicTrackUrl(String musicTrackUrl) {
        this.musicTrackUrl = musicTrackUrl;
    }
    
    public String getTextStyle() {
        return textStyle;
    }
    
    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }
    
    public String getConfig() {
        return config;
    }
    
    public void setConfig(String config) {
        this.config = config;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

// Made with Bob
