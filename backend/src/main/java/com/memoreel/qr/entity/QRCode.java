package com.memoreel.qr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a QR code for a published project.
 * 
 * QR codes enable physical-digital integration for memorials.
 * 
 * @author Memoreel Team
 */
@Entity
@Table(name = "qr_codes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long projectId;
    
    @Column(nullable = false, length = 500)
    private String imageUrl;
    
    @Column(nullable = false, length = 500)
    private String targetUrl; // The public URL the QR code points to
    
    @Column(nullable = false)
    private Integer size = 512; // QR code size in pixels
    
    @Column(length = 10)
    private String format = "PNG";
    
    @Column(length = 1)
    private String errorCorrection = "H"; // High error correction
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime regeneratedAt;
}

// Made with Bob
