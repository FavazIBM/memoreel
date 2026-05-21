package com.memoreel.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a user in the Memoreel system.
 * 
 * @author Memoreel Team
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(length = 500)
    private String avatarUrl;
    
    @Column(nullable = false)
    private Boolean emailVerified = false;
    
    @Column(length = 50)
    private String provider; // "local" or "google"
    
    @Column(length = 255)
    private String providerId; // Google user ID if OAuth
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime lastLoginAt;
}

// Made with Bob
