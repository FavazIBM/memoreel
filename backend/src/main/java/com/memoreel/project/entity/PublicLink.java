package com.memoreel.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a public link for a published project.
 * 
 * Public links provide permanent URLs for accessing published reels and memorials.
 * 
 * @author Memoreel Team
 */
@Entity
@Table(name = "public_links")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicLink {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long projectId;
    
    @Column(nullable = false, unique = true, length = 12)
    private String slug; // Random alphanumeric string (8-12 characters)
    
    @Column(nullable = false, length = 500)
    private String publicUrl; // Full public URL (e.g., https://memoreel.app/m/abc123xyz)
    
    @Column(nullable = false)
    private Long views = 0L;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime lastViewedAt;
}

// Made with Bob
