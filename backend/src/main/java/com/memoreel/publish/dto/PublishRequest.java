package com.memoreel.publish.dto;

import com.memoreel.project.entity.Privacy;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for publishing a project
 * Contains privacy setting for the published project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishRequest {
    
    @NotNull(message = "Privacy setting is required")
    private Privacy privacy;
}

// Made with Bob
