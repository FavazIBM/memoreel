package com.memoreel.media.entity;

import com.memoreel.common.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Lightweight media model used by the current in-memory backend flow.
 * This is intentionally not a JPA entity because JPA dependencies are not
 * configured in the current backend module.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    private Long id;
    private Long projectId;
    private MediaType type;
    private String url;
    private String thumbnailUrl;
    private Integer orderIndex;
    private Long size;
    private String mimeType;
    private String originalFilename;
    private LocalDateTime uploadedAt;
}

// Made with Bob
