package com.memoreel.video.batch;

import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.common.exception.VideoProcessingException;
import com.memoreel.media.entity.Media;
import com.memoreel.media.repository.MediaRepository;
import com.memoreel.project.entity.Project;
import com.memoreel.project.entity.ProjectStatus;
import com.memoreel.project.repository.ProjectRepository;
import com.memoreel.service.S3Service;
import com.memoreel.video.entity.Template;
import com.memoreel.video.entity.Video;
import com.memoreel.video.entity.VideoJob;
import com.memoreel.video.entity.VideoJobStatus;
import com.memoreel.video.repository.VideoJobRepository;
import com.memoreel.video.repository.VideoRepository;
import com.memoreel.video.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Asynchronous video processing job handler.
 * Manages the complete video generation workflow from job creation to completion.
 */
@Component
public class VideoProcessingJob {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoProcessingJob.class);
    
    private final VideoJobRepository videoJobRepository;
    private final ProjectRepository projectRepository;
    private final MediaRepository mediaRepository;
    private final VideoRepository videoRepository;
    private final TemplateService templateService;
    private final VideoProcessor videoProcessor;
    private final S3Service s3Service;
    
    public VideoProcessingJob(
            VideoJobRepository videoJobRepository,
            ProjectRepository projectRepository,
            MediaRepository mediaRepository,
            VideoRepository videoRepository,
            TemplateService templateService,
            VideoProcessor videoProcessor,
            S3Service s3Service
    ) {
        this.videoJobRepository = videoJobRepository;
        this.projectRepository = projectRepository;
        this.mediaRepository = mediaRepository;
        this.videoRepository = videoRepository;
        this.templateService = templateService;
        this.videoProcessor = videoProcessor;
        this.s3Service = s3Service;
    }
    
    /**
     * Process a video generation job asynchronously.
     * This method handles the complete video generation workflow.
     */
    @Async
    @Transactional
    public void processVideoJob(Long jobId) {
        logger.info("Starting video processing job: {}", jobId);
        
        VideoJob job = null;
        Project project = null;
        String tempDir = null;
        
        try {
            // Step 1: Fetch and validate job
            job = videoJobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Video job not found: " + jobId));
            
            if (job.getStatus() != VideoJobStatus.PENDING) {
                logger.warn("Job {} is not in PENDING status, skipping", jobId);
                return;
            }
            
            // Step 2: Update job status to PROCESSING
            job.setStatus(VideoJobStatus.PROCESSING);
            job.setStartedAt(LocalDateTime.now());
            videoJobRepository.save(job);
            logger.info("Job {} status updated to PROCESSING", jobId);
            
            // Step 3: Fetch project and validate
            project = projectRepository.findById(job.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + job.getProjectId()));
            
            // Step 4: Update project status to PROCESSING
            project.setStatus(ProjectStatus.PROCESSING);
            projectRepository.save(project);
            logger.info("Project {} status updated to PROCESSING", project.getId());
            
            // Step 5: Fetch media for the project
            List<Media> mediaList = mediaRepository.findByProjectIdOrderByOrderIndexAsc(project.getId());
            
            if (mediaList.isEmpty()) {
                throw new VideoProcessingException("No media found for project: " + project.getId());
            }
            
            logger.info("Found {} media items for project {}", mediaList.size(), project.getId());
            
            // Step 6: Select appropriate template
            Template template = templateService.selectTemplate(
                project.getOccasionType(),
                project.getMetadata() != null ? extractMood(project.getMetadata()) : null
            );
            logger.info("Selected template: {} for occasion: {}", template.getName(), project.getOccasionType());
            
            // Step 7: Create temporary directory for processing
            tempDir = createTempDirectory();
            logger.info("Created temp directory: {}", tempDir);
            
            // Step 8: Download media from S3 to temp directory
            downloadMediaFiles(mediaList, tempDir);
            
            // Step 9: Prepare metadata for video generation
            Map<String, String> metadata = prepareMetadata(project);
            
            // Step 10: Generate video using VideoProcessor
            logger.info("Starting video generation");
            String videoPath = videoProcessor.generateVideo(mediaList, template, metadata, tempDir);
            logger.info("Video generated successfully: {}", videoPath);
            
            // Step 11: Upload generated video to S3
            String s3Key = uploadVideoToS3(videoPath, project);
            String s3Url = s3Service.getFileUrl(s3Key);
            logger.info("Video uploaded to S3: {}", s3Url);
            
            // Step 12: Upload thumbnail to S3
            String thumbnailPath = Paths.get(tempDir, "thumbnail.jpg").toString();
            String thumbnailS3Key = uploadThumbnailToS3(thumbnailPath, project);
            String thumbnailUrl = s3Service.getFileUrl(thumbnailS3Key);
            logger.info("Thumbnail uploaded to S3: {}", thumbnailUrl);
            
            // Step 13: Get video duration
            int duration = getVideoDuration(videoPath);
            
            // Step 14: Save Video entity
            Video video = createVideoEntity(project, s3Url, s3Key, thumbnailUrl, duration);
            videoRepository.save(video);
            logger.info("Video entity saved: {}", video.getId());
            
            // Step 15: Update project status to COMPLETED
            project.setStatus(ProjectStatus.COMPLETED);
            project.setVideoId(video.getId());
            projectRepository.save(project);
            logger.info("Project {} status updated to COMPLETED", project.getId());
            
            // Step 16: Update job status to COMPLETED
            job.setStatus(VideoJobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            job.setResultVideoId(video.getId());
            videoJobRepository.save(job);
            logger.info("Job {} completed successfully", jobId);
            
        } catch (Exception e) {
            logger.error("Error processing video job {}", jobId, e);
            
            // Update job status to FAILED
            if (job != null) {
                job.setStatus(VideoJobStatus.FAILED);
                job.setErrorMessage(e.getMessage());
                job.setCompletedAt(LocalDateTime.now());
                videoJobRepository.save(job);
            }
            
            // Update project status to FAILED
            if (project != null) {
                project.setStatus(ProjectStatus.FAILED);
                projectRepository.save(project);
            }
            
        } finally {
            // Step 17: Clean up temp files
            if (tempDir != null) {
                cleanupTempDirectory(tempDir);
            }
        }
    }
    
    /**
     * Create a temporary directory for video processing.
     */
    private String createTempDirectory() throws Exception {
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String uniqueDir = "memoreel_" + UUID.randomUUID().toString();
        Path tempDir = Paths.get(tempDirPath, uniqueDir);
        Files.createDirectories(tempDir);
        return tempDir.toString();
    }
    
    /**
     * Download media files from S3 to local temp directory.
     */
    private void downloadMediaFiles(List<Media> mediaList, String tempDir) throws Exception {
        logger.info("Downloading {} media files from S3", mediaList.size());
        
        for (int i = 0; i < mediaList.size(); i++) {
            Media media = mediaList.get(i);
            String extension = getFileExtension(media.getFileName());
            String localPath = Paths.get(tempDir, "media_" + i + extension).toString();
            
            // Download from S3
            s3Service.downloadFile(media.getS3Key(), localPath);
            logger.info("Downloaded: {} -> {}", media.getS3Key(), localPath);
            
            // Update media object with local path for processing
            media.setS3Url(localPath); // Temporarily store local path
        }
    }
    
    /**
     * Prepare metadata for video generation.
     */
    private Map<String, String> prepareMetadata(Project project) {
        Map<String, String> metadata = new HashMap<>();
        
        // Add title
        metadata.put("title", project.getTitle());
        
        // Add credits if available
        if (project.getMetadata() != null && project.getMetadata().contains("credits")) {
            // Parse credits from project metadata
            metadata.put("credits", extractCredits(project.getMetadata()));
        }
        
        // Add occasion-specific text
        metadata.put("occasion", project.getOccasionType().toString());
        
        return metadata;
    }
    
    /**
     * Upload generated video to S3.
     */
    private String uploadVideoToS3(String videoPath, Project project) throws Exception {
        String s3Key = String.format("videos/%d/%s/final_video.mp4", 
            project.getUserId(), 
            project.getId());
        
        File videoFile = new File(videoPath);
        s3Service.uploadFile(videoFile, s3Key, "video/mp4");
        
        return s3Key;
    }
    
    /**
     * Upload thumbnail to S3.
     */
    private String uploadThumbnailToS3(String thumbnailPath, Project project) throws Exception {
        String s3Key = String.format("videos/%d/%s/thumbnail.jpg", 
            project.getUserId(), 
            project.getId());
        
        File thumbnailFile = new File(thumbnailPath);
        if (thumbnailFile.exists()) {
            s3Service.uploadFile(thumbnailFile, s3Key, "image/jpeg");
        }
        
        return s3Key;
    }
    
    /**
     * Get video duration in seconds.
     */
    private int getVideoDuration(String videoPath) {
        try {
            // This would use FFprobe or similar to get duration
            // For now, return a placeholder
            return 60; // TODO: Implement actual duration calculation
        } catch (Exception e) {
            logger.warn("Failed to get video duration, using default", e);
            return 60;
        }
    }
    
    /**
     * Create Video entity from generated video.
     */
    private Video createVideoEntity(Project project, String s3Url, String s3Key, 
                                   String thumbnailUrl, int duration) {
        Video video = new Video();
        video.setProjectId(project.getId());
        video.setUserId(project.getUserId());
        video.setS3Url(s3Url);
        video.setS3Key(s3Key);
        video.setThumbnailUrl(thumbnailUrl);
        video.setDuration(duration);
        video.setResolution("1920x1080");
        video.setFileSize(getFileSize(s3Key));
        return video;
    }
    
    /**
     * Get file size from S3.
     */
    private Long getFileSize(String s3Key) {
        try {
            // TODO: Implement actual S3 file size retrieval
            return 10485760L; // Placeholder: 10MB
        } catch (Exception e) {
            logger.warn("Failed to get file size", e);
            return 0L;
        }
    }
    
    /**
     * Clean up temporary directory and all files.
     */
    private void cleanupTempDirectory(String tempDir) {
        try {
            logger.info("Cleaning up temp directory: {}", tempDir);
            Path tempPath = Paths.get(tempDir);
            
            if (Files.exists(tempPath)) {
                Files.walk(tempPath)
                    .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            logger.warn("Failed to delete: {}", path, e);
                        }
                    });
            }
            
            logger.info("Temp directory cleaned up successfully");
        } catch (Exception e) {
            logger.error("Error cleaning up temp directory", e);
        }
    }
    
    /**
     * Extract mood from project metadata.
     */
    private String extractMood(String metadata) {
        // Simple extraction - in production, parse JSON properly
        if (metadata.contains("vibrant")) return "vibrant";
        if (metadata.contains("calm")) return "calm";
        if (metadata.contains("elegant")) return "elegant";
        if (metadata.contains("playful")) return "playful";
        if (metadata.contains("romantic")) return "romantic";
        return null;
    }
    
    /**
     * Extract credits from project metadata.
     */
    private String extractCredits(String metadata) {
        // Simple extraction - in production, parse JSON properly
        // Return default credits
        return "Created with Memoreel";
    }
    
    /**
     * Get file extension from filename.
     */
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }
}

// Made with Bob
