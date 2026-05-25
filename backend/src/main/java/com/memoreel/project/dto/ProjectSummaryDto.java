package com.memoreel.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectSummaryDto {
    private final Long id;
    private final String title;
    private final String type;
    private final String status;
    private final String thumbnail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}

// Made with Bob
