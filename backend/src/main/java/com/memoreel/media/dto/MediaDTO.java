package com.memoreel.media.dto;

import com.memoreel.media.entity.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for media information
 * Contains all media details for client consumption
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    
    private Long id;
    
    private Long projectId;
    
    private MediaType type;
    
    private String url;
    
    private String thumbnailUrl;
    
    private Integer orderIndex;
    
    private Long size;
    
    private LocalDateTime createdAt;
}

// Made with Bob
