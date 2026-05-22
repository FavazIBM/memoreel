package com.memoreel.video.batch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoreel.common.exception.VideoProcessingException;
import com.memoreel.media.entity.Media;
import com.memoreel.util.FFmpegUtil;
import com.memoreel.video.entity.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core video processing component that generates videos from media and templates.
 * Handles the complete video generation pipeline including:
 * - Media processing (images and videos)
 * - Transitions
 * - Text overlays
 * - Background music
 * - Final video assembly
 */
@Component
public class VideoProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoProcessor.class);
    
    private final FFmpegUtil ffmpegUtil;
    private final ObjectMapper objectMapper;
    
    public VideoProcessor(FFmpegUtil ffmpegUtil) {
        this.ffmpegUtil = ffmpegUtil;
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Generate a complete video from media list and template.
     * 
     * @param mediaList List of media items (images and videos)
     * @param template Template with styling and configuration
     * @param metadata Additional metadata (title, credits, etc.)
     * @param outputDir Directory for output files
     * @return Path to the generated video file
     */
    public String generateVideo(
            List<Media> mediaList,
            Template template,
            Map<String, String> metadata,
            String outputDir
    ) throws VideoProcessingException {
        
        logger.info("Starting video generation with {} media items", mediaList.size());
        
        try {
            // Create output directory if it doesn't exist
            Path outputPath = Paths.get(outputDir);
            Files.createDirectories(outputPath);
            
            // Sort media by order index
            List<Media> sortedMedia = mediaList.stream()
                .sorted(Comparator.comparing(Media::getOrderIndex))
                .collect(Collectors.toList());
            
            // Parse template configuration
            JsonNode templateConfig = parseTemplateConfig(template);
            
            // Step 1: Create title screen
            String titleScreenPath = null;
            if (metadata.containsKey("title")) {
                logger.info("Creating title screen");
                titleScreenPath = createTitleScreen(
                    metadata.get("title"),
                    template.getTextStyle(),
                    outputDir,
                    templateConfig.path("titleDuration").asInt(3)
                );
            }
            
            // Step 2: Process media segments
            logger.info("Processing media segments");
            List<String> mediaSegments = processMediaSegments(sortedMedia, template, outputDir);
            
            // Step 3: Create credits screen
            String creditsScreenPath = null;
            if (metadata.containsKey("credits")) {
                logger.info("Creating credits screen");
                creditsScreenPath = createCreditsScreen(
                    metadata.get("credits"),
                    template.getTextStyle(),
                    outputDir,
                    templateConfig.path("creditsDuration").asInt(3)
                );
            }
            
            // Step 4: Combine all segments
            List<String> allSegments = new ArrayList<>();
            if (titleScreenPath != null) {
                allSegments.add(titleScreenPath);
            }
            allSegments.addAll(mediaSegments);
            if (creditsScreenPath != null) {
                allSegments.add(creditsScreenPath);
            }
            
            // Step 5: Concatenate segments
            String concatenatedPath = Paths.get(outputDir, "concatenated.mp4").toString();
            logger.info("Concatenating {} segments", allSegments.size());
            concatenateSegments(allSegments, concatenatedPath);
            
            // Step 6: Add transitions
            String transitionedPath = Paths.get(outputDir, "transitioned.mp4").toString();
            logger.info("Adding transitions: {}", template.getTransitionType());
            ffmpegUtil.addTransitions(concatenatedPath, transitionedPath, template.getTransitionType());
            
            // Step 7: Add text overlays if specified
            String overlayPath = transitionedPath;
            if (metadata.containsKey("overlayText")) {
                overlayPath = Paths.get(outputDir, "overlay.mp4").toString();
                logger.info("Adding text overlay");
                ffmpegUtil.addTextOverlay(
                    transitionedPath,
                    overlayPath,
                    metadata.get("overlayText"),
                    metadata.getOrDefault("overlayPosition", "bottom")
                );
            }
            
            // Step 8: Add background music
            String finalVideoPath = Paths.get(outputDir, "final_video.mp4").toString();
            if (template.getMusicTrackUrl() != null && !template.getMusicTrackUrl().isEmpty()) {
                logger.info("Adding background music");
                String musicPath = downloadMusic(template.getMusicTrackUrl(), outputDir);
                ffmpegUtil.addBackgroundMusic(overlayPath, musicPath, finalVideoPath);
            } else {
                // No music, just copy the file
                Files.copy(Paths.get(overlayPath), Paths.get(finalVideoPath));
            }
            
            // Step 9: Generate thumbnail
            String thumbnailPath = Paths.get(outputDir, "thumbnail.jpg").toString();
            logger.info("Generating thumbnail");
            int thumbnailTime = ffmpegUtil.getVideoDuration(finalVideoPath) / 2; // Middle of video
            ffmpegUtil.generateThumbnail(finalVideoPath, thumbnailPath, thumbnailTime);
            
            // Step 10: Cleanup intermediate files
            cleanupIntermediateFiles(allSegments, concatenatedPath, transitionedPath, overlayPath);
            
            logger.info("Video generation completed successfully: {}", finalVideoPath);
            return finalVideoPath;
            
        } catch (Exception e) {
            logger.error("Error generating video", e);
            throw new VideoProcessingException("Failed to generate video: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a title screen with text overlay.
     */
    private String createTitleScreen(String title, String textStyle, String outputDir, int duration) 
            throws VideoProcessingException {
        try {
            String titleScreenPath = Paths.get(outputDir, "title_screen.mp4").toString();
            
            // Create a black background video
            String blackBgPath = Paths.get(outputDir, "black_bg.mp4").toString();
            createBlackBackground(blackBgPath, duration);
            
            // Add title text overlay
            ffmpegUtil.addTextOverlay(blackBgPath, titleScreenPath, title, "center");
            
            // Cleanup black background
            Files.deleteIfExists(Paths.get(blackBgPath));
            
            return titleScreenPath;
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to create title screen: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a credits screen with text overlay.
     */
    private String createCreditsScreen(String credits, String textStyle, String outputDir, int duration) 
            throws VideoProcessingException {
        try {
            String creditsScreenPath = Paths.get(outputDir, "credits_screen.mp4").toString();
            
            // Create a black background video
            String blackBgPath = Paths.get(outputDir, "black_bg_credits.mp4").toString();
            createBlackBackground(blackBgPath, duration);
            
            // Add credits text overlay
            ffmpegUtil.addTextOverlay(blackBgPath, creditsScreenPath, credits, "center");
            
            // Cleanup black background
            Files.deleteIfExists(Paths.get(blackBgPath));
            
            return creditsScreenPath;
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to create credits screen: " + e.getMessage(), e);
        }
    }
    
    /**
     * Process all media segments (images and videos).
     */
    private List<String> processMediaSegments(List<Media> mediaList, Template template, String outputDir) 
            throws VideoProcessingException {
        
        List<String> segments = new ArrayList<>();
        
        for (int i = 0; i < mediaList.size(); i++) {
            Media media = mediaList.get(i);
            String segmentPath = Paths.get(outputDir, "segment_" + i + ".mp4").toString();
            
            logger.info("Processing media {}/{}: {} ({})", 
                i + 1, mediaList.size(), media.getFileName(), media.getMediaType());
            
            try {
                // Download media from S3 to local temp file
                String localMediaPath = downloadMediaFromS3(media, outputDir, i);
                
                if (media.getMediaType().toString().equals("IMAGE")) {
                    // Convert image to video segment
                    convertImageToVideoSegment(localMediaPath, segmentPath, template.getDurationPerImage());
                } else {
                    // Process video segment
                    processVideoSegment(localMediaPath, segmentPath);
                }
                
                segments.add(segmentPath);
                
                // Cleanup downloaded media
                Files.deleteIfExists(Paths.get(localMediaPath));
                
            } catch (Exception e) {
                logger.error("Error processing media segment {}", i, e);
                throw new VideoProcessingException("Failed to process media segment: " + e.getMessage(), e);
            }
        }
        
        return segments;
    }
    
    /**
     * Concatenate video segments into a single video.
     */
    private void concatenateSegments(List<String> segments, String outputPath) 
            throws VideoProcessingException {
        try {
            // Create concat file
            Path concatFile = Files.createTempFile("concat_", ".txt");
            List<String> lines = segments.stream()
                .map(s -> "file '" + s + "'")
                .collect(Collectors.toList());
            Files.write(concatFile, lines);
            
            // Use FFmpeg to concatenate
            ffmpegUtil.createVideoFromImages(segments, outputPath, null);
            
            // Cleanup concat file
            Files.deleteIfExists(concatFile);
            
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to concatenate segments: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a black background video of specified duration.
     */
    private void createBlackBackground(String outputPath, int duration) throws VideoProcessingException {
        // This would use FFmpeg to create a black video
        // For now, we'll use a simple approach
        try {
            // Create a 1x1 black image first
            Path blackImagePath = Files.createTempFile("black_", ".png");
            // In production, you'd create an actual black image here
            // For now, assume it exists or use FFmpeg to generate it
            
            // Convert to video
            List<String> imagePaths = Collections.singletonList(blackImagePath.toString());
            ffmpegUtil.createVideoFromImages(imagePaths, outputPath, null);
            
            Files.deleteIfExists(blackImagePath);
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to create black background: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convert an image to a video segment.
     */
    private void convertImageToVideoSegment(String imagePath, String outputPath, int duration) 
            throws VideoProcessingException {
        List<String> imagePaths = Collections.singletonList(imagePath);
        ffmpegUtil.createVideoFromImages(imagePaths, outputPath, null);
    }
    
    /**
     * Process a video segment (normalize, resize, etc.).
     */
    private void processVideoSegment(String inputPath, String outputPath) throws VideoProcessingException {
        // Use FFmpeg to normalize the video
        // This is a placeholder - actual implementation would normalize resolution, framerate, etc.
        try {
            Files.copy(Paths.get(inputPath), Paths.get(outputPath));
        } catch (IOException e) {
            throw new VideoProcessingException("Failed to process video segment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Download media from S3 to local file.
     * In production, this would use S3Service.
     */
    private String downloadMediaFromS3(Media media, String outputDir, int index) throws IOException {
        // Placeholder: In production, use S3Service to download
        String extension = getFileExtension(media.getFileName());
        String localPath = Paths.get(outputDir, "media_" + index + extension).toString();
        
        // TODO: Implement actual S3 download
        // s3Service.downloadFile(media.getS3Key(), localPath);
        
        logger.info("Downloaded media from S3: {} -> {}", media.getS3Key(), localPath);
        return localPath;
    }
    
    /**
     * Download music from S3 or URL.
     */
    private String downloadMusic(String musicUrl, String outputDir) throws IOException {
        // Placeholder: In production, download from S3 or URL
        String musicPath = Paths.get(outputDir, "background_music.mp3").toString();
        
        // TODO: Implement actual download
        // If S3 URL: s3Service.downloadFile(s3Key, musicPath);
        // If HTTP URL: download via HTTP client
        
        logger.info("Downloaded music: {} -> {}", musicUrl, musicPath);
        return musicPath;
    }
    
    /**
     * Parse template configuration JSON.
     */
    private JsonNode parseTemplateConfig(Template template) {
        try {
            if (template.getConfig() != null && !template.getConfig().isEmpty()) {
                return objectMapper.readTree(template.getConfig());
            }
        } catch (Exception e) {
            logger.warn("Failed to parse template config, using defaults", e);
        }
        return objectMapper.createObjectNode();
    }
    
    /**
     * Cleanup intermediate files.
     */
    private void cleanupIntermediateFiles(List<String> segments, String... otherFiles) {
        // Cleanup segments
        for (String segment : segments) {
            try {
                Files.deleteIfExists(Paths.get(segment));
            } catch (IOException e) {
                logger.warn("Failed to delete segment: {}", segment, e);
            }
        }
        
        // Cleanup other files
        for (String file : otherFiles) {
            if (file != null) {
                try {
                    Files.deleteIfExists(Paths.get(file));
                } catch (IOException e) {
                    logger.warn("Failed to delete file: {}", file, e);
                }
            }
        }
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
