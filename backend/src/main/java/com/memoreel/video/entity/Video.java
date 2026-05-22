package com.memoreel.video.entity;

import com.memoreel.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;
    
    @Column(nullable = false)
    private Integer duration;
    
    @Column(length = 20)
    private String resolution;
    
    @Column(nullable = false)
    private Long size;
    
    @Column(length = 50)
    private String format;
    
    @Column(name = "frame_rate")
    private Integer frameRate;
    
    @Column(name = "bitrate")
    private Integer bitrate;
    
    @Column(name = "codec", length = 50)
    private String codec;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

// Made with Bob
