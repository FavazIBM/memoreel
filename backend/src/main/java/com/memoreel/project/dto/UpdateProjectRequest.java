package com.memoreel.project.dto;

import com.memoreel.project.entity.OccasionType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for updating project details
 * Only title and metadata can be updated
 * Status changes are handled through separate endpoints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {
    
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;
    
    private OccasionType occasionType;
    
    private String occasionDate;
    
    @Size(max = 100, message = "Recipient name cannot exceed 100 characters")
    private String recipientName;
    
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;
    
    private Map<String, Object> metadata;
}

// Made with Bob
