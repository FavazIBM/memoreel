package com.memoreel.project.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ProjectRecord {
    private Long id;
    private Long userId;
    private String title;
    private String type;
    private String status;
    private String privacy;
    private Map<String, Object> metadata;
    private List<Map<String, Object>> media;
    private Map<String, Object> video;
    private Map<String, Object> videoJob;
    private Map<String, Object> publicLink;
    private Map<String, Object> qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}

// Made with Bob
