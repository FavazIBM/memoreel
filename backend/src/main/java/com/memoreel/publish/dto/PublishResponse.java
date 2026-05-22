package com.memoreel.publish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for publish response
 * Contains public URL, slug, QR code URL, and publish timestamp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishResponse {
    
    private Long projectId;
    
    private String publicUrl;
    
    private String slug;
    
    private String qrCodeUrl;
    
    private LocalDateTime publishedAt;
}

// Made with Bob
