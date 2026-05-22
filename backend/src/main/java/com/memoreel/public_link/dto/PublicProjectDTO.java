package com.memoreel.public_link.dto;

import com.memoreel.project.entity.OccasionType;
import com.memoreel.project.entity.Privacy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for public project view
 * Contains project details, video, media thumbnails, and author info
 * Excludes sensitive information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicProjectDTO {
    
    private Long id;
    
    private String title;
    
    private OccasionType occasionType;
    
    private String occasionDate;
    
    private String recipientName;
    
    private String message;
    
    private Privacy privacy;
    
    private String videoUrl;
    
    private Integer videoDuration;
    
    private List<MediaThumbnail> mediaThumbnails;
    
    private AuthorInfo author;
    
    private LocalDateTime publishedAt;
    
    private Long viewCount;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaThumbnail {
        private String thumbnailUrl;
        private String type;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorInfo {
        private String name;
        private String avatarUrl;
    }
}

// Made with Bob
