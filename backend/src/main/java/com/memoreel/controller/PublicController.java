package com.memoreel.controller;

import com.memoreel.common.dto.ApiResponse;
import com.memoreel.public_link.dto.PublicProjectDTO;
import com.memoreel.service.PublicLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public controller
 * Handles public project access (no authentication required)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    
    private final PublicLinkService publicLinkService;
    
    /**
     * Get public project by slug
     * No authentication required
     * 
     * @param slug project slug
     * @return public project DTO
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PublicProjectDTO>> getPublicProject(@PathVariable String slug) {
        log.info("Public project request for slug: {}", slug);
        
        PublicProjectDTO publicProjectDTO = publicLinkService.getPublicProject(slug);
        
        ApiResponse<PublicProjectDTO> response = ApiResponse.<PublicProjectDTO>builder()
                .success(true)
                .message("Project retrieved successfully")
                .data(publicProjectDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
