package com.memoreel.project.dto;

import com.memoreel.project.entity.OccasionType;
import com.memoreel.project.entity.Privacy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Occasion type is required")
    private OccasionType occasionType;
    
    private LocalDate occasionDate;
    
    private Privacy privacy;
    
    private Map<String, Object> metadata;
}

// Made with Bob
