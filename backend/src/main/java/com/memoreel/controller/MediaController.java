package com.memoreel.controller;

import com.memoreel.common.dto.ApiResponse;
import com.memoreel.media.dto.MediaDTO;
import com.memoreel.media.dto.MediaUploadResponse;
import com.memoreel.media.dto.ReorderMediaRequest;
import com.memoreel.security.SecurityUser;
import com.memoreel.service.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Media controller
 * Handles media upload, reordering, and deletion
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {
    
    private final MediaService mediaService;
    
    /**
     * Upload media to project
     * 
     * @param securityUser authenticated user
     * @param file media file
     * @param projectId project ID
     * @return media upload response
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MediaUploadResponse>> uploadMedia(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") Long projectId) {
        log.info("Upload media request for project ID: {} by user ID: {}", 
                projectId, securityUser.getId());
        
        MediaUploadResponse mediaUploadResponse = mediaService.uploadMedia(
                securityUser.getId(), projectId, file);
        
        ApiResponse<MediaUploadResponse> response = ApiResponse.<MediaUploadResponse>builder()
                .success(true)
                .message("Media uploaded successfully")
                .data(mediaUploadResponse)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get project media
     * 
     * @param securityUser authenticated user
     * @param projectId project ID
     * @return list of media DTOs
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<MediaDTO>>> getProjectMedia(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long projectId) {
        log.info("Get media request for project ID: {} by user ID: {}", 
                projectId, securityUser.getId());
        
        List<MediaDTO> mediaList = mediaService.getProjectMedia(securityUser.getId(), projectId);
        
        ApiResponse<List<MediaDTO>> response = ApiResponse.<List<MediaDTO>>builder()
                .success(true)
                .message("Media retrieved successfully")
                .data(mediaList)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Reorder media item
     * 
     * @param securityUser authenticated user
     * @param id media ID
     * @param request reorder request
     * @return updated media DTO
     */
    @PutMapping("/{id}/reorder")
    public ResponseEntity<ApiResponse<MediaDTO>> reorderMedia(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id,
            @Valid @RequestBody ReorderMediaRequest request) {
        log.info("Reorder media request for ID: {} by user ID: {}", id, securityUser.getId());
        
        MediaDTO mediaDTO = mediaService.reorderMedia(securityUser.getId(), id, request);
        
        ApiResponse<MediaDTO> response = ApiResponse.<MediaDTO>builder()
                .success(true)
                .message("Media reordered successfully")
                .data(mediaDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete media
     * 
     * @param securityUser authenticated user
     * @param id media ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id) {
        log.info("Delete media request for ID: {} by user ID: {}", id, securityUser.getId());
        
        mediaService.deleteMedia(securityUser.getId(), id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Media deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
