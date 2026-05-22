package com.memoreel.media.dto;

import com.memoreel.media.entity.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for media upload response
 * Contains media details after successful upload
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResponse {
    
    private Long id;
    
    private Long projectId;
    
    private MediaType type;
    
    private String url;
    
    private String thumbnailUrl;
    
    private Integer orderIndex;
    
    private Long size;
    
    private String filename;
    
    private LocalDateTime uploadedAt;
}

// Made with Bob
