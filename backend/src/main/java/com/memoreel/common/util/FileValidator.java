package com.memoreel.common.util;

import com.memoreel.common.exception.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class FileValidator {
    
    // Image validation
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    
    // Video validation
    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo", "video/webm"
    );
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB
    
    /**
     * Validate image file
     */
    public void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is required");
        }
        
        validateFileSize(file, MAX_IMAGE_SIZE);
        validateMimeType(file, ALLOWED_IMAGE_TYPES);
        validateFileName(file);
    }
    
    /**
     * Validate video file
     */
    public void validateVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is required");
        }
        
        validateFileSize(file, MAX_VIDEO_SIZE);
        validateMimeType(file, ALLOWED_VIDEO_TYPES);
        validateFileName(file);
    }
    
    /**
     * Validate file size
     */
    public void validateFileSize(MultipartFile file, long maxSize) {
        if (file.getSize() > maxSize) {
            throw new FileUploadException(
                    String.format("File size exceeds maximum allowed size of %d MB", maxSize / (1024 * 1024))
            );
        }
        
        if (file.getSize() == 0) {
            throw new FileUploadException("File is empty");
        }
    }
    
    /**
     * Validate MIME type
     */
    public void validateMimeType(MultipartFile file, List<String> allowedTypes) {
        String contentType = file.getContentType();
        
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new FileUploadException(
                    String.format("Invalid file type. Allowed types: %s", String.join(", ", allowedTypes))
            );
        }
    }
    
    /**
     * Validate file name
     */
    private void validateFileName(MultipartFile file) {
        String filename = file.getOriginalFilename();
        
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileUploadException("File name is required");
        }
        
        // Check for path traversal attempts
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new FileUploadException("Invalid file name");
        }
        
        // Check file extension
        if (!filename.contains(".")) {
            throw new FileUploadException("File must have an extension");
        }
    }
    
    /**
     * Get file extension
     */
    public String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
    
    /**
     * Check if file is an image
     */
    public boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }
    
    /**
     * Check if file is a video
     */
    public boolean isVideo(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && ALLOWED_VIDEO_TYPES.contains(contentType.toLowerCase());
    }
}

// Made with Bob
