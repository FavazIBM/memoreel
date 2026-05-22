package com.memoreel.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for video information
 * Contains all video details for client consumption
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
    
    private Long id;
    
    private Long projectId;
    
    private String url;
    
    private Integer duration;
    
    private String resolution;
    
    private Long size;
    
    private LocalDateTime createdAt;
}

// Made with Bob
