package com.memoreel.qr.entity;

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
@Table(name = "qr_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;
    
    @Column(name = "qr_image_url", nullable = false, length = 500)
    private String qrImageUrl;
    
    @Column(name = "public_url", nullable = false, length = 500)
    private String publicUrl;
    
    @Column(name = "size")
    private Integer size;
    
    @Column(name = "format", length = 10)
    private String format;
    
    @Column(name = "error_correction_level", length = 10)
    private String errorCorrectionLevel;
    
    @Column(name = "scan_count")
    private Long scanCount;
    
    @Column(name = "last_scanned_at")
    private LocalDateTime lastScannedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (scanCount == null) {
            scanCount = 0L;
        }
        if (size == null) {
            size = 512;
        }
        if (format == null) {
            format = "PNG";
        }
        if (errorCorrectionLevel == null) {
            errorCorrectionLevel = "M";
        }
    }
    
    public void incrementScanCount() {
        this.scanCount++;
        this.lastScannedAt = LocalDateTime.now();
    }
}

// Made with Bob
