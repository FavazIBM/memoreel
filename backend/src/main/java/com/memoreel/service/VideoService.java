package com.memoreel.service;

import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.common.exception.UnauthorizedException;
import com.memoreel.common.exception.ValidationException;
import com.memoreel.media.repository.MediaRepository;
import com.memoreel.project.entity.Project;
import com.memoreel.project.entity.ProjectStatus;
import com.memoreel.project.repository.ProjectRepository;
import com.memoreel.video.batch.VideoProcessingJob;
import com.memoreel.video.dto.GenerateVideoRequest;
import com.memoreel.video.dto.VideoDTO;
import com.memoreel.video.dto.VideoStatusResponse;
import com.memoreel.video.entity.Video;
import com.memoreel.video.entity.VideoJob;
import com.memoreel.video.entity.VideoJobStatus;
import com.memoreel.video.repository.VideoJobRepository;
import com.memoreel.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Video service
 * Handles video generation, status tracking, and retrieval
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {
    
    private final VideoRepository videoRepository;
    private final VideoJobRepository videoJobRepository;
    private final ProjectRepository projectRepository;
    private final MediaRepository mediaRepository;
    private final VideoProcessingJob videoProcessingJob;
    
    /**
     * Generate video for project
     * 
     * @param userId the user ID
     * @param request generate video request
     * @return video status response
     * @throws ValidationException if project status doesn't allow video generation
     */
    @Transactional
    public VideoStatusResponse generateVideo(Long userId, GenerateVideoRequest request) {
        log.info("Generating video for project ID: {} by user ID: {}", request.getProjectId(), userId);
        
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateProjectOwnership(project, userId);
        
        // Only DRAFT projects can have videos generated
        if (project.getStatus() != ProjectStatus.DRAFT) {
            throw new ValidationException("Videos can only be generated for draft projects");
        }
        
        // Validate that project has media
        long mediaCount = mediaRepository.countByProjectId(project.getId());
        if (mediaCount == 0) {
            throw new ValidationException("Project must have at least one media item to generate video");
        }
        
        // Create video job
        VideoJob job = VideoJob.builder()
                .project(project)
                .status(VideoJobStatus.PENDING)
                .progress(0)
                .templateId(request.getTemplateId())
                .build();
        
        job = videoJobRepository.save(job);
        
        // Update project status to PROCESSING (will be updated by job)
        project.setStatus(ProjectStatus.PROCESSING);
        projectRepository.save(project);
        
        log.info("Video generation job created with ID: {}", job.getId());
        
        // Trigger async video processing job
        videoProcessingJob.processVideoJob(job.getId());
        
        return VideoStatusResponse.builder()
                .jobId(job.getId())
                .projectId(project.getId())
                .status(job.getStatus())
                .progress(job.getProgress())
                .estimatedCompletion(LocalDateTime.now().plusMinutes(5)) // Estimate 5 minutes
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
    
    /**
     * Get video generation status
     * 
     * @param userId the user ID
     * @param jobId the job ID
     * @return video status response
     */
    @Transactional(readOnly = true)
    public VideoStatusResponse getVideoStatus(Long userId, Long jobId) {
        log.info("Fetching video status for job ID: {} by user ID: {}", jobId, userId);
        
        VideoJob job = videoJobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Video job not found"));
        
        validateProjectOwnership(job.getProject(), userId);
        
        return VideoStatusResponse.builder()
                .jobId(job.getId())
                .projectId(job.getProject().getId())
                .status(job.getStatus())
                .progress(job.getProgress())
                .estimatedCompletion(job.getEstimatedCompletion())
                .errorMessage(job.getErrorMessage())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
    
    /**
     * Get project video
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @return video DTO
     * @throws ResourceNotFoundException if video not found
     */
    @Transactional(readOnly = true)
    public VideoDTO getProjectVideo(Long userId, Long projectId) {
        log.info("Fetching video for project ID: {} by user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateProjectOwnership(project, userId);
        
        Video video = videoRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found for this project"));
        
        return convertToDTO(video);
    }
    
    /**
     * Regenerate video for project
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @return video status response
     * @throws ValidationException if project status doesn't allow regeneration
     */
    @Transactional
    public VideoStatusResponse regenerateVideo(Long userId, Long projectId) {
        log.info("Regenerating video for project ID: {} by user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateProjectOwnership(project, userId);
        
        // Only COMPLETED or FAILED projects can have videos regenerated
        if (project.getStatus() != ProjectStatus.COMPLETED && 
            project.getStatus() != ProjectStatus.FAILED) {
            throw new ValidationException("Videos can only be regenerated for completed or failed projects");
        }
        
        // Validate that project has media
        long mediaCount = mediaRepository.countByProjectId(project.getId());
        if (mediaCount == 0) {
            throw new ValidationException("Project must have at least one media item to regenerate video");
        }
        
        // Get previous job to reuse template
        VideoJob previousJob = videoJobRepository.findLatestByProjectId(projectId)
                .orElse(null);
        
        Long templateId = previousJob != null ? previousJob.getTemplateId() : null;
        
        // Create new video job
        VideoJob job = VideoJob.builder()
                .project(project)
                .status(VideoJobStatus.PENDING)
                .progress(0)
                .templateId(templateId)
                .build();
        
        job = videoJobRepository.save(job);
        
        // Update project status to PROCESSING (will be updated by job)
        project.setStatus(ProjectStatus.PROCESSING);
        projectRepository.save(project);
        
        log.info("Video regeneration job created with ID: {}", job.getId());
        
        // Trigger async video processing job
        videoProcessingJob.processVideoJob(job.getId());
        
        return VideoStatusResponse.builder()
                .jobId(job.getId())
                .projectId(project.getId())
                .status(job.getStatus())
                .progress(job.getProgress())
                .estimatedCompletion(LocalDateTime.now().plusMinutes(5))
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
    
    /**
     * Validate project ownership
     * 
     * @param project the project
     * @param userId the user ID
     */
    private void validateProjectOwnership(Project project, Long userId) {
        if (!project.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to access this project");
        }
    }
    
    /**
     * Convert Video entity to DTO
     * 
     * @param video the video entity
     * @return video DTO
     */
    private VideoDTO convertToDTO(Video video) {
        return VideoDTO.builder()
                .id(video.getId())
                .projectId(video.getProject().getId())
                .url(video.getUrl())
                .duration(video.getDuration())
                .resolution(video.getResolution())
                .size(video.getSize())
                .createdAt(video.getCreatedAt())
                .build();
    }
}

// Made with Bob
