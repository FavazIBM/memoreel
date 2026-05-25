package com.memoreel.project.controller;

import com.memoreel.api.ApiResponse;
import com.memoreel.project.dto.CreateProjectRequest;
import com.memoreel.project.dto.ProjectDetailDto;
import com.memoreel.project.dto.ProjectListResponse;
import com.memoreel.project.dto.ProjectSummaryDto;
import com.memoreel.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ApiResponse<ProjectSummaryDto> createProject(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @Valid @RequestBody CreateProjectRequest request
    ) {
        return ApiResponse.success(projectService.createProject(userId, request), "Project created successfully");
    }

    @GetMapping("/projects")
    public ApiResponse<ProjectListResponse> listProjects(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type
    ) {
        return ApiResponse.success(projectService.listProjects(userId, status, type, page, limit), "Projects fetched successfully");
    }

    @GetMapping("/projects/{id}")
    public ApiResponse<ProjectDetailDto> getProject(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @PathVariable Long id
    ) {
        return ApiResponse.success(projectService.getProject(userId, id), "Project fetched successfully");
    }

    @PostMapping(value = "/projects/{id}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProjectDetailDto> uploadMedia(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @PathVariable Long id,
            @RequestPart("files") List<MultipartFile> files
    ) {
        return ApiResponse.success(projectService.uploadMedia(userId, id, files), "Media uploaded successfully");
    }

    @GetMapping("/projects/{id}/media/{fileName:.+}")
    public ResponseEntity<Resource> getProjectMedia(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @PathVariable Long id,
            @PathVariable String fileName
    ) {
        Path filePath = projectService.resolveProjectMediaPath(userId, id, fileName);
        return ResponseEntity.ok()
                .contentType(resolveMediaType(fileName))
                .body(new FileSystemResource(filePath));
    }

    @PostMapping("/projects/{id}/generate")
    public ApiResponse<ProjectDetailDto> generateVideo(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @PathVariable Long id
    ) {
        return ApiResponse.success(projectService.generateVideo(userId, id), "Video generation completed successfully");
    }

    @GetMapping("/videos/{fileName:.+}")
    public ResponseEntity<Resource> getGeneratedVideo(@PathVariable String fileName) {
        Long projectId = extractProjectId(fileName);
        Path filePath = projectService.resolveGeneratedVideoPath(projectId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(new FileSystemResource(filePath));
    }

    @PostMapping("/projects/{id}/publish")
    public ApiResponse<ProjectDetailDto> publishProject(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @PathVariable Long id
    ) {
        return ApiResponse.success(projectService.publishProject(userId, id), "Project published successfully");
    }

    @GetMapping("/public/{slug}")
    public ApiResponse<ProjectDetailDto> getPublicProject(@PathVariable String slug) {
        return ApiResponse.success(projectService.getPublicProject(slug), "Public project fetched successfully");
    }

    private MediaType resolveMediaType(String fileName) {
        String normalized = fileName.toLowerCase();
        if (normalized.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (normalized.endsWith(".jpg") || normalized.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        if (normalized.endsWith(".mp4")) {
            return MediaType.parseMediaType("video/mp4");
        }
        if (normalized.endsWith(".mov")) {
            return MediaType.parseMediaType("video/quicktime");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private Long extractProjectId(String fileName) {
        try {
            String normalized = fileName.replace("project-", "").replace(".mp4", "").trim();
            return Long.parseLong(normalized);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid generated video identifier");
        }
    }
}

// Made with Bob
