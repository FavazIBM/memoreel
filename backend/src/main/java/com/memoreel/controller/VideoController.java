package com.memoreel.controller;

import com.memoreel.common.dto.ApiResponse;
import com.memoreel.security.SecurityUser;
import com.memoreel.service.VideoService;
import com.memoreel.video.dto.GenerateVideoRequest;
import com.memoreel.video.dto.VideoDTO;
import com.memoreel.video.dto.VideoStatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Video controller
 * Handles video generation, status tracking, and retrieval
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {
    
    private final VideoService videoService;
    
    /**
     * Generate video for project
     * 
     * @param securityUser authenticated user
     * @param request generate video request
     * @return video status response
     */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<VideoStatusResponse>> generateVideo(
            @AuthenticationPrincipal SecurityUser securityUser,
            @Valid @RequestBody GenerateVideoRequest request) {
        log.info("Generate video request for project ID: {} by user ID: {}", 
                request.getProjectId(), securityUser.getId());
        
        VideoStatusResponse statusResponse = videoService.generateVideo(securityUser.getId(), request);
        
        ApiResponse<VideoStatusResponse> response = ApiResponse.<VideoStatusResponse>builder()
                .success(true)
                .message("Video generation started")
                .data(statusResponse)
                .build();
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
    
    /**
     * Get video generation status
     * 
     * @param securityUser authenticated user
     * @param jobId job ID
     * @return video status response
     */
    @GetMapping("/status/{jobId}")
    public ResponseEntity<ApiResponse<VideoStatusResponse>> getVideoStatus(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long jobId) {
        log.info("Get video status request for job ID: {} by user ID: {}", 
                jobId, securityUser.getId());
        
        VideoStatusResponse statusResponse = videoService.getVideoStatus(securityUser.getId(), jobId);
        
        ApiResponse<VideoStatusResponse> response = ApiResponse.<VideoStatusResponse>builder()
                .success(true)
                .message("Video status retrieved successfully")
                .data(statusResponse)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get project video
     * 
     * @param securityUser authenticated user
     * @param projectId project ID
     * @return video DTO
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<VideoDTO>> getProjectVideo(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long projectId) {
        log.info("Get video request for project ID: {} by user ID: {}", 
                projectId, securityUser.getId());
        
        VideoDTO videoDTO = videoService.getProjectVideo(securityUser.getId(), projectId);
        
        ApiResponse<VideoDTO> response = ApiResponse.<VideoDTO>builder()
                .success(true)
                .message("Video retrieved successfully")
                .data(videoDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Regenerate video for project
     * 
     * @param securityUser authenticated user
     * @param projectId project ID
     * @return video status response
     */
    @PostMapping("/regenerate")
    public ResponseEntity<ApiResponse<VideoStatusResponse>> regenerateVideo(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestParam Long projectId) {
        log.info("Regenerate video request for project ID: {} by user ID: {}", 
                projectId, securityUser.getId());
        
        VideoStatusResponse statusResponse = videoService.regenerateVideo(securityUser.getId(), projectId);
        
        ApiResponse<VideoStatusResponse> response = ApiResponse.<VideoStatusResponse>builder()
                .success(true)
                .message("Video regeneration started")
                .data(statusResponse)
                .build();
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}

// Made with Bob
