package com.memoreel.util;

import com.memoreel.common.exception.VideoProcessingException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for FFmpeg video processing operations.
 * Handles video creation, transitions, overlays, and audio mixing.
 */
@Component
public class FFmpegUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(FFmpegUtil.class);
    
    @Value("${ffmpeg.path:/usr/bin/ffmpeg}")
    private String ffmpegPath;
    
    @Value("${ffprobe.path:/usr/bin/ffprobe}")
    private String ffprobePath;
    
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;
    private FFmpegExecutor executor;
    
    @PostConstruct
    public void init() throws IOException {
        this.ffmpeg = new FFmpeg(ffmpegPath);
        this.ffprobe = new FFprobe(ffprobePath);
        this.executor = new FFmpegExecutor(ffmpeg, ffprobe);
        logger.info("FFmpeg initialized: {}", ffmpegPath);
    }
    
    /**
     * Create video from a list of images with background music.
     * Each image is displayed for 3 seconds.
     */
    public void createVideoFromImages(List<String> imagePaths, String outputPath, String musicPath) 
            throws VideoProcessingException {
        try {
            logger.info("Creating video from {} images", imagePaths.size());
            
            // Create a temporary concat file
            Path concatFile = createImageConcatFile(imagePaths, 3);
            
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(concatFile.toString())
                .addExtraArgs("-f", "concat")
                .addExtraArgs("-safe", "0")
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .setVideoResolution(1920, 1080)
                .setVideoFrameRate(30)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();
            
            executor.createJob(builder).run();
            
            // Add music if provided
            if (musicPath != null && !musicPath.isEmpty()) {
                String tempOutput = outputPath + ".temp.mp4";
                Files.move(Paths.get(outputPath), Paths.get(tempOutput));
                addBackgroundMusic(tempOutput, musicPath, outputPath);
                Files.deleteIfExists(Paths.get(tempOutput));
            }
            
            // Cleanup
            Files.deleteIfExists(concatFile);
            
            logger.info("Video created successfully: {}", outputPath);
        } catch (Exception e) {
            logger.error("Error creating video from images", e);
            throw new VideoProcessingException("Failed to create video from images: " + e.getMessage());
        }
    }
    
    /**
     * Create video from mixed media (images and videos) with text overlays.
     */
    public void createVideoFromMixed(List<com.memoreel.media.entity.Media> mediaList, 
                                     String outputPath, 
                                     String musicPath,
                                     Map<String, String> textOverlays) throws VideoProcessingException {
        try {
            logger.info("Creating video from {} mixed media items", mediaList.size());
            
            List<String> processedSegments = new ArrayList<>();
            
            // Process each media item
            for (int i = 0; i < mediaList.size(); i++) {
                com.memoreel.media.entity.Media media = mediaList.get(i);
                String segmentPath = outputPath + ".segment_" + i + ".mp4";
                
                if (media.getMediaType().toString().equals("IMAGE")) {
                    // Convert image to 3-second video
                    convertImageToVideo(media.getS3Url(), segmentPath, 3);
                } else {
                    // Process video (normalize and trim if needed)
                    processVideoSegment(media.getS3Url(), segmentPath);
                }
                
                processedSegments.add(segmentPath);
            }
            
            // Concatenate all segments
            String concatenatedPath = outputPath + ".concat.mp4";
            concatenateVideos(processedSegments, concatenatedPath);
            
            // Add transitions
            String transitionedPath = outputPath + ".transition.mp4";
            addTransitions(concatenatedPath, transitionedPath, "fade");
            
            // Add text overlays if provided
            String overlayPath = transitionedPath;
            if (textOverlays != null && !textOverlays.isEmpty()) {
                overlayPath = outputPath + ".overlay.mp4";
                addMultipleTextOverlays(transitionedPath, overlayPath, textOverlays);
            }
            
            // Add background music
            if (musicPath != null && !musicPath.isEmpty()) {
                addBackgroundMusic(overlayPath, musicPath, outputPath);
            } else {
                Files.move(Paths.get(overlayPath), Paths.get(outputPath));
            }
            
            // Cleanup temporary files
            cleanupTempFiles(processedSegments);
            Files.deleteIfExists(Paths.get(concatenatedPath));
            Files.deleteIfExists(Paths.get(transitionedPath));
            if (!overlayPath.equals(transitionedPath)) {
                Files.deleteIfExists(Paths.get(overlayPath));
            }
            
            logger.info("Mixed media video created successfully: {}", outputPath);
        } catch (Exception e) {
            logger.error("Error creating video from mixed media", e);
            throw new VideoProcessingException("Failed to create video from mixed media: " + e.getMessage());
        }
    }
    
    /**
     * Add fade transitions between video segments.
     */
    public void addTransitions(String inputPath, String outputPath, String transitionType) 
            throws VideoProcessingException {
        try {
            logger.info("Adding {} transitions to video", transitionType);
            
            // For now, implement fade transition
            // More complex transitions would require xfade filter
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .addExtraArgs("-vf", "fade=in:0:30,fade=out:st=" + (getVideoDuration(inputPath) - 1) + ":d=1")
                .setAudioCodec("aac")
                .done();
            
            executor.createJob(builder).run();
            
            logger.info("Transitions added successfully");
        } catch (Exception e) {
            logger.error("Error adding transitions", e);
            throw new VideoProcessingException("Failed to add transitions: " + e.getMessage());
        }
    }
    
    /**
     * Add text overlay to video at specified position.
     */
    public void addTextOverlay(String inputPath, String outputPath, String text, String position) 
            throws VideoProcessingException {
        try {
            logger.info("Adding text overlay: {}", text);
            
            // Escape special characters in text
            String escapedText = text.replace(":", "\\:").replace("'", "\\'");
            
            // Position mapping: "top", "center", "bottom"
            String drawTextFilter = buildDrawTextFilter(escapedText, position);
            
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .addExtraArgs("-vf", drawTextFilter)
                .setAudioCodec("copy")
                .done();
            
            executor.createJob(builder).run();
            
            logger.info("Text overlay added successfully");
        } catch (Exception e) {
            logger.error("Error adding text overlay", e);
            throw new VideoProcessingException("Failed to add text overlay: " + e.getMessage());
        }
    }
    
    /**
     * Add background music to video with proper volume mixing.
     */
    public void addBackgroundMusic(String videoPath, String musicPath, String outputPath) 
            throws VideoProcessingException {
        try {
            logger.info("Adding background music to video");
            
            int videoDuration = getVideoDuration(videoPath);
            
            FFmpegBuilder builder = new FFmpegBuilder()
                .addInput(videoPath)
                .addInput(musicPath)
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .setAudioCodec("aac")
                .addExtraArgs("-filter_complex", 
                    "[1:a]afade=t=in:st=0:d=2,afade=t=out:st=" + (videoDuration - 2) + ":d=2,volume=0.3[music];" +
                    "[0:a][music]amix=inputs=2:duration=first:dropout_transition=2[aout]")
                .addExtraArgs("-map", "0:v")
                .addExtraArgs("-map", "[aout]")
                .addExtraArgs("-shortest")
                .done();
            
            executor.createJob(builder).run();
            
            logger.info("Background music added successfully");
        } catch (Exception e) {
            logger.error("Error adding background music", e);
            throw new VideoProcessingException("Failed to add background music: " + e.getMessage());
        }
    }
    
    /**
     * Get video duration in seconds.
     */
    public int getVideoDuration(String videoPath) throws VideoProcessingException {
        try {
            FFmpegProbeResult probeResult = ffprobe.probe(videoPath);
            double duration = probeResult.getFormat().duration;
            return (int) Math.ceil(duration);
        } catch (IOException e) {
            logger.error("Error getting video duration", e);
            throw new VideoProcessingException("Failed to get video duration: " + e.getMessage());
        }
    }
    
    /**
     * Generate thumbnail from video at specified time.
     */
    public void generateThumbnail(String videoPath, String thumbnailPath, int timeSeconds) 
            throws VideoProcessingException {
        try {
            logger.info("Generating thumbnail at {}s", timeSeconds);
            
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath)
                .addExtraArgs("-ss", String.valueOf(timeSeconds))
                .addOutput(thumbnailPath)
                .setFrames(1)
                .setVideoResolution(1920, 1080)
                .done();
            
            executor.createJob(builder).run();
            
            logger.info("Thumbnail generated successfully");
        } catch (Exception e) {
            logger.error("Error generating thumbnail", e);
            throw new VideoProcessingException("Failed to generate thumbnail: " + e.getMessage());
        }
    }
    
    // Helper methods
    
    private Path createImageConcatFile(List<String> imagePaths, int durationPerImage) throws IOException {
        Path concatFile = Files.createTempFile("concat_", ".txt");
        List<String> lines = new ArrayList<>();
        
        for (String imagePath : imagePaths) {
            lines.add("file '" + imagePath + "'");
            lines.add("duration " + durationPerImage);
        }
        // Add last image again for proper duration
        if (!imagePaths.isEmpty()) {
            lines.add("file '" + imagePaths.get(imagePaths.size() - 1) + "'");
        }
        
        Files.write(concatFile, lines);
        return concatFile;
    }
    
    private void convertImageToVideo(String imagePath, String outputPath, int duration) 
            throws VideoProcessingException {
        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                .addInput(imagePath)
                .addExtraArgs("-loop", "1")
                .addExtraArgs("-t", String.valueOf(duration))
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .setVideoResolution(1920, 1080)
                .setVideoFrameRate(30)
                .addExtraArgs("-vf", "scale=1920:1080:force_original_aspect_ratio=decrease,pad=1920:1080:(ow-iw)/2:(oh-ih)/2")
                .done();
            
            executor.createJob(builder).run();
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to convert image to video: " + e.getMessage());
        }
    }
    
    private void processVideoSegment(String inputPath, String outputPath) throws VideoProcessingException {
        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .setVideoResolution(1920, 1080)
                .addExtraArgs("-vf", "scale=1920:1080:force_original_aspect_ratio=decrease,pad=1920:1080:(ow-iw)/2:(oh-ih)/2")
                .setAudioCodec("aac")
                .done();
            
            executor.createJob(builder).run();
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to process video segment: " + e.getMessage());
        }
    }
    
    private void concatenateVideos(List<String> videoPaths, String outputPath) throws VideoProcessingException {
        try {
            Path concatFile = Files.createTempFile("concat_videos_", ".txt");
            List<String> lines = new ArrayList<>();
            
            for (String videoPath : videoPaths) {
                lines.add("file '" + videoPath + "'");
            }
            
            Files.write(concatFile, lines);
            
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(concatFile.toString())
                .addExtraArgs("-f", "concat")
                .addExtraArgs("-safe", "0")
                .addOutput(outputPath)
                .setVideoCodec("libx264")
                .setVideoPixelFormat("yuv420p")
                .setAudioCodec("aac")
                .done();
            
            executor.createJob(builder).run();
            
            Files.deleteIfExists(concatFile);
        } catch (Exception e) {
            throw new VideoProcessingException("Failed to concatenate videos: " + e.getMessage());
        }
    }
    
    private void addMultipleTextOverlays(String inputPath, String outputPath, Map<String, String> textOverlays) 
            throws VideoProcessingException {
        String currentInput = inputPath;
        String currentOutput = outputPath;
        int overlayCount = 0;
        
        for (Map.Entry<String, String> entry : textOverlays.entrySet()) {
            if (overlayCount > 0) {
                currentInput = currentOutput;
                currentOutput = outputPath + ".temp_" + overlayCount + ".mp4";
            }
            
            addTextOverlay(currentInput, currentOutput, entry.getValue(), entry.getKey());
            
            if (overlayCount > 0) {
                try {
                    Files.deleteIfExists(Paths.get(currentInput));
                } catch (IOException e) {
                    logger.warn("Failed to delete temp file: {}", currentInput);
                }
            }
            
            overlayCount++;
        }
        
        // Rename final output if needed
        if (!currentOutput.equals(outputPath)) {
            try {
                Files.move(Paths.get(currentOutput), Paths.get(outputPath));
            } catch (IOException e) {
                throw new VideoProcessingException("Failed to move final output: " + e.getMessage());
            }
        }
    }
    
    private String buildDrawTextFilter(String text, String position) {
        String fontsize = "48";
        String fontcolor = "white";
        String x = "(w-text_w)/2"; // center horizontally
        String y;
        
        switch (position.toLowerCase()) {
            case "top":
                y = "50";
                break;
            case "bottom":
                y = "h-th-50";
                break;
            case "center":
            default:
                y = "(h-text_h)/2";
                break;
        }
        
        return String.format("drawtext=text='%s':fontsize=%s:fontcolor=%s:x=%s:y=%s:box=1:boxcolor=black@0.5:boxborderw=5",
            text, fontsize, fontcolor, x, y);
    }
    
    private void cleanupTempFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                logger.warn("Failed to delete temp file: {}", filePath);
            }
        }
    }
}

// Made with Bob
