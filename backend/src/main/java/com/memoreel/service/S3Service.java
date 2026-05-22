package com.memoreel.service;

import com.memoreel.common.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * S3 service for file operations
 * Handles file upload, deletion, and presigned URL generation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    @Value("${aws.s3.region}")
    private String region;
    
    /**
     * Upload file to S3
     * 
     * @param file the file to upload
     * @param folder the folder path in S3
     * @return the public URL of the uploaded file
     * @throws FileUploadException if upload fails
     */
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String key = folder + "/" + fileName;
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            
            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            String url = String.format("https://%s.s3.%s.amazonaws.com/%s", 
                    bucketName, region, key);
            
            log.info("File uploaded successfully: {}", url);
            return url;
            
        } catch (IOException e) {
            log.error("Failed to upload file to S3", e);
            throw new FileUploadException("Failed to upload file: " + e.getMessage());
        } catch (S3Exception e) {
            log.error("S3 error during file upload", e);
            throw new FileUploadException("S3 error: " + e.getMessage());
        }
    }
    
    /**
     * Delete file from S3
     * 
     * @param fileUrl the URL of the file to delete
     */
    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            s3Client.deleteObject(deleteObjectRequest);
            
            log.info("File deleted successfully: {}", key);
            
        } catch (S3Exception e) {
            log.error("Failed to delete file from S3: {}", fileUrl, e);
            // Don't throw exception, just log the error
        }
    }
    
    /**
     * Generate presigned URL for temporary access
     * 
     * @param fileUrl the URL of the file
     * @param expirationMinutes expiration time in minutes
     * @return presigned URL
     */
    public String generatePresignedUrl(String fileUrl, int expirationMinutes) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expirationMinutes))
                    .getObjectRequest(getObjectRequest)
                    .build();
            
            try (S3Presigner presigner = S3Presigner.create()) {
                PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
                return presignedRequest.url().toString();
            }
            
        } catch (S3Exception e) {
            log.error("Failed to generate presigned URL", e);
            throw new FileUploadException("Failed to generate presigned URL: " + e.getMessage());
        }
    }
    
    /**
     * Generate unique file name
     * 
     * @param originalFilename the original file name
     * @return unique file name
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
    
    /**
     * Extract S3 key from URL
     * 
     * @param url the S3 URL
     * @return the S3 key
     */
    private String extractKeyFromUrl(String url) {
        // URL format: https://bucket-name.s3.region.amazonaws.com/key
        String[] parts = url.split(".amazonaws.com/");
        if (parts.length > 1) {
            return parts[1];
        }
        throw new IllegalArgumentException("Invalid S3 URL: " + url);
    }
}

// Made with Bob
