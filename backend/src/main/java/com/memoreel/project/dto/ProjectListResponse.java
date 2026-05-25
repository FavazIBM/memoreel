package com.memoreel.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ProjectListResponse {
    private final List<ProjectSummaryDto> projects;
    private final Map<String, Object> pagination;
}

// Made with Bob
