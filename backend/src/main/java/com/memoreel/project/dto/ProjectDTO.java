package com.memoreel.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.memoreel.project.entity.OccasionType;
import com.memoreel.project.entity.Privacy;
import com.memoreel.project.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {
    
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private OccasionType occasionType;
    private LocalDate occasionDate;
    private ProjectStatus status;
    private Privacy privacy;
    private String thumbnailUrl;
    private Integer mediaCount;
    private Boolean hasVideo;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Made with Bob
