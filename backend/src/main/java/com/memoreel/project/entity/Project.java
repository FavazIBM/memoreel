package com.memoreel.project.entity;

import com.memoreel.common.enums.OccasionType;
import com.memoreel.common.enums.Privacy;
import com.memoreel.common.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entity representing a project (memory reel) in the Memoreel system.
 * 
 * A project is the central unit for storing media, text, generated video,
 * and occasion-specific metadata.
 * 
 * @author Memoreel Team
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
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OccasionType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProjectStatus status = ProjectStatus.DRAFT;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Privacy privacy = Privacy.PUBLIC;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime publishedAt;
}

// Made with Bob
