package com.memoreel.service;

import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.common.exception.UnauthorizedException;
import com.memoreel.common.util.SlugGenerator;
import com.memoreel.media.entity.Media;
import com.memoreel.media.repository.MediaRepository;
import com.memoreel.project.entity.Privacy;
import com.memoreel.project.entity.Project;
import com.memoreel.public_link.dto.PublicProjectDTO;
import com.memoreel.public_link.entity.PublicLink;
import com.memoreel.public_link.repository.PublicLinkRepository;
import com.memoreel.video.entity.Video;
import com.memoreel.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Public link service
 * Handles public project access and link generation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PublicLinkService {
    
    private final PublicLinkRepository publicLinkRepository;
    private final MediaRepository mediaRepository;
    private final VideoRepository videoRepository;
    private final SlugGenerator slugGenerator;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    /**
     * Create public link for project
     * 
     * @param project the project
     * @return public URL
     */
    @Transactional
    public String createPublicLink(Project project) {
        log.info("Creating public link for project ID: {}", project.getId());
        
        // Check if link already exists
        PublicLink existingLink = publicLinkRepository.findByProjectId(project.getId())
                .orElse(null);
        
        if (existingLink != null) {
            log.info("Public link already exists for project: {}", project.getId());
            return baseUrl + "/public/" + existingLink.getSlug();
        }
        
        // Generate unique slug
        String slug = generateUniqueSlug(project.getTitle());
        
        // Update project slug
        project.setSlug(slug);
        
        // Create public link
        PublicLink publicLink = PublicLink.builder()
                .project(project)
                .slug(slug)
                .viewCount(0L)
                .build();
        
        publicLinkRepository.save(publicLink);
        
        log.info("Public link created with slug: {}", slug);
        
        return baseUrl + "/public/" + slug;
    }
    
    /**
     * Get public project by slug
     * 
     * @param slug the project slug
     * @return public project DTO
     * @throws ResourceNotFoundException if project not found
     * @throws UnauthorizedException if project is private
     */
    @Transactional
    public PublicProjectDTO getPublicProject(String slug) {
        log.info("Fetching public project with slug: {}", slug);
        
        PublicLink publicLink = publicLinkRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        Project project = publicLink.getProject();
        
        // Check privacy setting
        if (project.getPrivacy() == Privacy.PRIVATE) {
            throw new UnauthorizedException("This project is private");
        }
        
        // Increment view count
        publicLink.setViewCount(publicLink.getViewCount() + 1);
        publicLinkRepository.save(publicLink);
        
        // Get video
        Video video = videoRepository.findByProjectId(project.getId()).orElse(null);
        
        // Get media thumbnails
        List<Media> mediaList = mediaRepository.findByProjectIdOrderByOrderIndexAsc(project.getId());
        List<PublicProjectDTO.MediaThumbnail> thumbnails = mediaList.stream()
                .map(media -> PublicProjectDTO.MediaThumbnail.builder()
                        .thumbnailUrl(media.getThumbnailUrl())
                        .type(media.getType().name())
                        .build())
                .collect(Collectors.toList());
        
        // Build author info
        PublicProjectDTO.AuthorInfo authorInfo = PublicProjectDTO.AuthorInfo.builder()
                .name(project.getUser().getName())
                .avatarUrl(project.getUser().getAvatarUrl())
                .build();
        
        return PublicProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .occasionType(project.getOccasionType())
                .occasionDate(project.getOccasionDate())
                .recipientName(project.getRecipientName())
                .message(project.getMessage())
                .privacy(project.getPrivacy())
                .videoUrl(video != null ? video.getUrl() : null)
                .videoDuration(video != null ? video.getDuration() : null)
                .mediaThumbnails(thumbnails)
                .author(authorInfo)
                .publishedAt(project.getPublishedAt())
                .viewCount(publicLink.getViewCount())
                .build();
    }
    
    /**
     * Generate unique slug for project
     * 
     * @param title the project title
     * @return unique slug
     */
    private String generateUniqueSlug(String title) {
        String baseSlug = slugGenerator.generateSlug(title);
        String slug = baseSlug;
        int counter = 1;
        
        // Ensure uniqueness
        while (publicLinkRepository.findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
}

// Made with Bob
