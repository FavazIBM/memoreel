package com.memoreel.project.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entity representing a project (memory reel).
 * 
 * A project contains all information about a memory reel including
 * its type, status, privacy settings, and occasion-specific metadata.
 */
@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OccasionType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.DRAFT;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Privacy privacy = Privacy.PUBLIC;
    
    @Column(columnDefinition = "jsonb")
    private String metadata;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    /**
     * Validates if the project can transition to the target status.
     * 
     * @param targetStatus the desired status
     * @throws IllegalStateException if transition is not allowed
     */
    public void validateStatusTransition(ProjectStatus targetStatus) {
        if (!this.status.canTransitionTo(targetStatus)) {
            throw new IllegalStateException(
                String.format("Cannot transition from %s to %s", this.status, targetStatus)
            );
        }
    }
    
    /**
     * Updates the project status with validation.
     * 
     * @param newStatus the new status
     */
    public void updateStatus(ProjectStatus newStatus) {
        validateStatusTransition(newStatus);
        this.status = newStatus;
        
        if (newStatus == ProjectStatus.PUBLISHED && this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }
}

// Made with Bob
