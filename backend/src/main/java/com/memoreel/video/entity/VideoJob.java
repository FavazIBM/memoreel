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
@Table(name = "video_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VideoJobStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "progress_percentage")
    private Integer progressPercentage;
    
    @Column(name = "external_job_id", length = 255)
    private String externalJobId;
    
    @Column(name = "retry_count")
    private Integer retryCount;
    
    @Column(name = "estimated_completion")
    private LocalDateTime estimatedCompletion;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = VideoJobStatus.PENDING;
        }
        if (progressPercentage == null) {
            progressPercentage = 0;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
    }
}

// Made with Bob
