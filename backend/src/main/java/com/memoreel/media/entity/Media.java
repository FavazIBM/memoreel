package com.memoreel.media.entity;

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
@Table(name = "media", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"project_id", "order_index"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaType type;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    @Column(nullable = false)
    private Long size;
    
    @Column(length = 100)
    private String filename;
    
    @Column(name = "mime_type", length = 100)
    private String mimeType;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "width")
    private Integer width;
    
    @Column(name = "height")
    private Integer height;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

// Made with Bob
