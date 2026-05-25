package com.memoreel.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ProjectDetailDto {
    private final Long id;
    private final String title;
    private final String type;
    private final String status;
    private final String privacy;
    private final Map<String, Object> metadata;
    private final List<Map<String, Object>> media;
    private final Map<String, Object> video;
    private final Map<String, Object> videoJob;
    private final Map<String, Object> publicLink;
    private final Map<String, Object> qrCode;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime publishedAt;
}

// Made with Bob
