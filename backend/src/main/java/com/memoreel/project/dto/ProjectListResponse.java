package com.memoreel.project.dto;

import com.memoreel.common.dto.PaginationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Wrapper for paginated project list
 * Contains list of projects and pagination metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListResponse {
    
    private List<ProjectDTO> projects;
    
    private PaginationResponse pagination;
}

// Made with Bob
