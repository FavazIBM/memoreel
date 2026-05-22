package com.memoreel.controller;

import com.memoreel.common.dto.ApiResponse;
import com.memoreel.project.dto.CreateProjectRequest;
import com.memoreel.project.dto.ProjectDTO;
import com.memoreel.project.dto.ProjectListResponse;
import com.memoreel.project.dto.UpdateProjectRequest;
import com.memoreel.project.entity.Privacy;
import com.memoreel.project.entity.ProjectStatus;
import com.memoreel.publish.dto.PublishRequest;
import com.memoreel.publish.dto.PublishResponse;
import com.memoreel.security.SecurityUser;
import com.memoreel.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Project controller
 * Handles project CRUD operations and publishing
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    
    /**
     * Create a new project
     * 
     * @param securityUser authenticated user
     * @param request create project request
     * @return created project DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(
            @AuthenticationPrincipal SecurityUser securityUser,
            @Valid @RequestBody CreateProjectRequest request) {
        log.info("Create project request by user ID: {}", securityUser.getId());
        
        ProjectDTO projectDTO = projectService.createProject(securityUser.getId(), request);
        
        ApiResponse<ProjectDTO> response = ApiResponse.<ProjectDTO>builder()
                .success(true)
                .message("Project created successfully")
                .data(projectDTO)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get user's projects with pagination and filtering
     * 
     * @param securityUser authenticated user
     * @param page page number (default: 0)
     * @param limit items per page (default: 10)
     * @param status filter by status (optional)
     * @param sort sort field (default: createdAt)
     * @return paginated project list
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ProjectListResponse>> getUserProjects(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(defaultValue = "createdAt") String sort) {
        log.info("Get projects request by user ID: {} (page: {}, limit: {})", 
                securityUser.getId(), page, limit);
        
        ProjectListResponse projectList = projectService.getUserProjects(
                securityUser.getId(), page, limit, status, sort);
        
        ApiResponse<ProjectListResponse> response = ApiResponse.<ProjectListResponse>builder()
                .success(true)
                .message("Projects retrieved successfully")
                .data(projectList)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get project by ID
     * 
     * @param securityUser authenticated user
     * @param id project ID
     * @return project DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProjectById(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id) {
        log.info("Get project request for ID: {} by user ID: {}", id, securityUser.getId());
        
        ProjectDTO projectDTO = projectService.getProjectById(securityUser.getId(), id);
        
        ApiResponse<ProjectDTO> response = ApiResponse.<ProjectDTO>builder()
                .success(true)
                .message("Project retrieved successfully")
                .data(projectDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update project
     * 
     * @param securityUser authenticated user
     * @param id project ID
     * @param request update project request
     * @return updated project DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request) {
        log.info("Update project request for ID: {} by user ID: {}", id, securityUser.getId());
        
        ProjectDTO projectDTO = projectService.updateProject(securityUser.getId(), id, request);
        
        ApiResponse<ProjectDTO> response = ApiResponse.<ProjectDTO>builder()
                .success(true)
                .message("Project updated successfully")
                .data(projectDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete project
     * 
     * @param securityUser authenticated user
     * @param id project ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id) {
        log.info("Delete project request for ID: {} by user ID: {}", id, securityUser.getId());
        
        projectService.deleteProject(securityUser.getId(), id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Project deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Publish project
     * 
     * @param securityUser authenticated user
     * @param id project ID
     * @param request publish request
     * @return publish response with public URL and QR code
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<PublishResponse>> publishProject(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id,
            @Valid @RequestBody PublishRequest request) {
        log.info("Publish project request for ID: {} by user ID: {}", id, securityUser.getId());
        
        PublishResponse publishResponse = projectService.publishProject(securityUser.getId(), id, request);
        
        ApiResponse<PublishResponse> response = ApiResponse.<PublishResponse>builder()
                .success(true)
                .message("Project published successfully")
                .data(publishResponse)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update project privacy
     * 
     * @param securityUser authenticated user
     * @param id project ID
     * @param privacy new privacy setting
     * @return updated project DTO
     */
    @PutMapping("/{id}/privacy")
    public ResponseEntity<ApiResponse<ProjectDTO>> updatePrivacy(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long id,
            @RequestBody Privacy privacy) {
        log.info("Update privacy request for project ID: {} to {} by user ID: {}", 
                id, privacy, securityUser.getId());
        
        ProjectDTO projectDTO = projectService.updatePrivacy(securityUser.getId(), id, privacy);
        
        ApiResponse<ProjectDTO> response = ApiResponse.<ProjectDTO>builder()
                .success(true)
                .message("Privacy updated successfully")
                .data(projectDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
