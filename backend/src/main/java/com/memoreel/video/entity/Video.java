package com.memoreel.video.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a generated video in the Memoreel system.
 * 
 * Videos are the final output of the video generation process.
 * 
 * @author Memoreel Team
 */
@Entity
@Table(name = "videos")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long projectId;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column(length = 500)
    private String thumbnailUrl;
    
    @Column(nullable = false)
    private Integer duration; // Duration in seconds
    
    @Column(length = 20)
    private String resolution = "1080p";
    
    @Column(nullable = false)
    private Long size; // File size in bytes
    
    @Column(length = 10)
    private String format = "mp4";
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

// Made with Bob
