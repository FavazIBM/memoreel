package com.memoreel.video.entity;

import com.memoreel.common.enums.VideoJobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a video generation job in the Memoreel system.
 * 
 * Video jobs track the asynchronous video generation process.
 * 
 * @author Memoreel Team
 */
@Entity
@Table(name = "video_jobs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long projectId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VideoJobStatus status = VideoJobStatus.QUEUED;
    
    @Column
    private Integer progress; // Progress percentage (0-100)
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime startedAt;
    
    @Column
    private LocalDateTime completedAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private Integer estimatedTime; // Estimated completion time in seconds
}

// Made with Bob
