package com.memoreel.video.dto;

import com.memoreel.video.entity.VideoJobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for video generation status
 * Contains job status, progress, and estimated completion time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoStatusResponse {
    
    private Long jobId;
    
    private Long projectId;
    
    private VideoJobStatus status;
    
    private Integer progress;
    
    private LocalDateTime estimatedCompletion;
    
    private String errorMessage;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}

// Made with Bob
