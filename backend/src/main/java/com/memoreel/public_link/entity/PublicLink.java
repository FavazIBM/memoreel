package com.memoreel.public_link.entity;

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
@Table(name = "public_links", uniqueConstraints = {
    @UniqueConstraint(columnNames = "slug")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicLink {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;
    
    @Column(nullable = false, unique = true, length = 20)
    private String slug;
    
    @Column(name = "view_count")
    private Long viewCount;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "password_hash", length = 255)
    private String passwordHash;
    
    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (viewCount == null) {
            viewCount = 0L;
        }
        if (isActive == null) {
            isActive = true;
        }
    }
    
    public void incrementViewCount() {
        this.viewCount++;
        this.lastAccessedAt = LocalDateTime.now();
    }
    
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isPasswordProtected() {
        return passwordHash != null && !passwordHash.isEmpty();
    }
}

// Made with Bob
