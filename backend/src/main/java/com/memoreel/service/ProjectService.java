package com.memoreel.service;

import com.memoreel.auth.entity.User;
import com.memoreel.auth.repository.UserRepository;
import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.common.exception.UnauthorizedException;
import com.memoreel.common.exception.ValidationException;
import com.memoreel.project.dto.CreateProjectRequest;
import com.memoreel.project.dto.ProjectDTO;
import com.memoreel.project.dto.ProjectListResponse;
import com.memoreel.project.dto.UpdateProjectRequest;
import com.memoreel.project.entity.Privacy;
import com.memoreel.project.entity.Project;
import com.memoreel.project.entity.ProjectStatus;
import com.memoreel.project.repository.ProjectRepository;
import com.memoreel.publish.dto.PublishRequest;
import com.memoreel.publish.dto.PublishResponse;
import com.memoreel.common.dto.PaginationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project service
 * Handles project CRUD operations and publishing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PublicLinkService publicLinkService;
    private final QRCodeService qrCodeService;
    
    /**
     * Create a new project
     * 
     * @param userId the user ID
     * @param request create project request
     * @return created project DTO
     */
    @Transactional
    public ProjectDTO createProject(Long userId, CreateProjectRequest request) {
        log.info("Creating project for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Project project = Project.builder()
                .user(user)
                .title(request.getTitle())
                .occasionType(request.getOccasionType())
                .occasionDate(request.getOccasionDate())
                .recipientName(request.getRecipientName())
                .message(request.getMessage())
                .status(ProjectStatus.DRAFT)
                .privacy(Privacy.PRIVATE)
                .build();
        
        project = projectRepository.save(project);
        
        log.info("Project created successfully with ID: {}", project.getId());
        
        return convertToDTO(project);
    }
    
    /**
     * Get project by ID
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @return project DTO
     * @throws ResourceNotFoundException if project not found
     * @throws UnauthorizedException if user doesn't own the project
     */
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long userId, Long projectId) {
        log.info("Fetching project ID: {} for user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateOwnership(project, userId);
        
        return convertToDTO(project);
    }
    
    /**
     * Get user's projects with pagination and filtering
     * 
     * @param userId the user ID
     * @param page page number (0-indexed)
     * @param limit items per page
     * @param status filter by status (optional)
     * @param sort sort field (default: createdAt)
     * @return paginated project list
     */
    @Transactional(readOnly = true)
    public ProjectListResponse getUserProjects(Long userId, int page, int limit, 
                                               ProjectStatus status, String sort) {
        log.info("Fetching projects for user ID: {} (page: {}, limit: {})", userId, page, limit);
        
        Sort sortOrder = Sort.by(Sort.Direction.DESC, sort != null ? sort : "createdAt");
        Pageable pageable = PageRequest.of(page, limit, sortOrder);
        
        Page<Project> projectPage;
        if (status != null) {
            projectPage = projectRepository.findByUserIdAndStatus(userId, status, pageable);
        } else {
            projectPage = projectRepository.findByUserId(userId, pageable);
        }
        
        List<ProjectDTO> projects = projectPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        PaginationResponse pagination = PaginationResponse.builder()
                .currentPage(page)
                .totalPages(projectPage.getTotalPages())
                .totalItems(projectPage.getTotalElements())
                .itemsPerPage(limit)
                .build();
        
        return ProjectListResponse.builder()
                .projects(projects)
                .pagination(pagination)
                .build();
    }
    
    /**
     * Update project
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @param request update project request
     * @return updated project DTO
     * @throws ValidationException if project status doesn't allow editing
     */
    @Transactional
    public ProjectDTO updateProject(Long userId, Long projectId, UpdateProjectRequest request) {
        log.info("Updating project ID: {} for user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateOwnership(project, userId);
        
        // Only DRAFT projects can be edited
        if (project.getStatus() != ProjectStatus.DRAFT) {
            throw new ValidationException("Only draft projects can be edited");
        }
        
        // Update fields if provided
        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
        }
        
        if (request.getOccasionType() != null) {
            project.setOccasionType(request.getOccasionType());
        }
        
        if (request.getOccasionDate() != null) {
            project.setOccasionDate(request.getOccasionDate());
        }
        
        if (request.getRecipientName() != null) {
            project.setRecipientName(request.getRecipientName());
        }
        
        if (request.getMessage() != null) {
            project.setMessage(request.getMessage());
        }
        
        if (request.getMetadata() != null) {
            project.setMetadata(request.getMetadata());
        }
        
        project = projectRepository.save(project);
        
        log.info("Project updated successfully: {}", project.getId());
        
        return convertToDTO(project);
    }
    
    /**
     * Delete project
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @throws ValidationException if project status doesn't allow deletion
     */
    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        log.info("Deleting project ID: {} for user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateOwnership(project, userId);
        
        // Only DRAFT and FAILED projects can be deleted
        if (project.getStatus() != ProjectStatus.DRAFT && 
            project.getStatus() != ProjectStatus.FAILED) {
            throw new ValidationException("Only draft or failed projects can be deleted");
        }
        
        projectRepository.delete(project);
        
        log.info("Project deleted successfully: {}", projectId);
    }
    
    /**
     * Publish project
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @param request publish request
     * @return publish response with public URL and QR code
     * @throws ValidationException if project is not completed
     */
    @Transactional
    public PublishResponse publishProject(Long userId, Long projectId, PublishRequest request) {
        log.info("Publishing project ID: {} for user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateOwnership(project, userId);
        
        // Only COMPLETED projects can be published
        if (project.getStatus() != ProjectStatus.COMPLETED) {
            throw new ValidationException("Only completed projects can be published");
        }
        
        // Update privacy and status
        project.setPrivacy(request.getPrivacy());
        project.setStatus(ProjectStatus.PUBLISHED);
        project.setPublishedAt(LocalDateTime.now());
        
        project = projectRepository.save(project);
        
        // Create public link
        String publicUrl = publicLinkService.createPublicLink(project);
        
        // Generate QR code
        String qrCodeUrl = qrCodeService.generateQRCode(project, publicUrl);
        
        log.info("Project published successfully: {}", project.getId());
        
        return PublishResponse.builder()
                .projectId(project.getId())
                .publicUrl(publicUrl)
                .slug(project.getSlug())
                .qrCodeUrl(qrCodeUrl)
                .publishedAt(project.getPublishedAt())
                .build();
    }
    
    /**
     * Update project privacy
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @param privacy new privacy setting
     * @return updated project DTO
     */
    @Transactional
    public ProjectDTO updatePrivacy(Long userId, Long projectId, Privacy privacy) {
        log.info("Updating privacy for project ID: {} to {}", projectId, privacy);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateOwnership(project, userId);
        
        project.setPrivacy(privacy);
        project = projectRepository.save(project);
        
        log.info("Privacy updated successfully for project: {}", project.getId());
        
        return convertToDTO(project);
    }
    
    /**
     * Validate project ownership
     * 
     * @param project the project
     * @param userId the user ID
     * @throws UnauthorizedException if user doesn't own the project
     */
    private void validateOwnership(Project project, Long userId) {
        if (!project.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to access this project");
        }
    }
    
    /**
     * Convert Project entity to DTO
     * 
     * @param project the project entity
     * @return project DTO
     */
    private ProjectDTO convertToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .userId(project.getUser().getId())
                .title(project.getTitle())
                .slug(project.getSlug())
                .occasionType(project.getOccasionType())
                .occasionDate(project.getOccasionDate())
                .recipientName(project.getRecipientName())
                .message(project.getMessage())
                .status(project.getStatus())
                .privacy(project.getPrivacy())
                .videoUrl(project.getVideoUrl())
                .thumbnailUrl(project.getThumbnailUrl())
                .metadata(project.getMetadata())
                .publishedAt(project.getPublishedAt())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}

// Made with Bob
