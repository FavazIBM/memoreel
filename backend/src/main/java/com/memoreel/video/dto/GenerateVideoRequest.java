package com.memoreel.video.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for video generation request
 * Contains project ID and optional template ID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateVideoRequest {
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    private Long templateId;
}

// Made with Bob
