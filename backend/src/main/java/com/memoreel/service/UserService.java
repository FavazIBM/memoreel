package com.memoreel.service;

import com.memoreel.auth.dto.ChangePasswordRequest;
import com.memoreel.auth.dto.UpdateProfileRequest;
import com.memoreel.auth.dto.UserDTO;
import com.memoreel.auth.entity.User;
import com.memoreel.auth.repository.UserRepository;
import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.common.exception.UnauthorizedException;
import com.memoreel.common.exception.ValidationException;
import com.memoreel.common.util.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * User service
 * Handles user profile operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;
    private final FileValidator fileValidator;
    
    /**
     * Get user by ID
     * 
     * @param userId the user ID
     * @return user DTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return convertToDTO(user);
    }
    
    /**
     * Update user profile
     * 
     * @param userId the user ID
     * @param request update profile request
     * @return updated user DTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional
    public UserDTO updateProfile(Long userId, UpdateProfileRequest request) {
        log.info("Updating profile for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Update fields if provided
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        
        user = userRepository.save(user);
        
        log.info("Profile updated successfully for user: {}", user.getEmail());
        
        return convertToDTO(user);
    }
    
    /**
     * Change user password
     * 
     * @param userId the user ID
     * @param request change password request
     * @throws ResourceNotFoundException if user not found
     * @throws UnauthorizedException if old password is incorrect
     */
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        log.info("Changing password for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }
        
        // Validate new password
        if (request.getNewPassword().length() < 8) {
            throw new ValidationException("New password must be at least 8 characters long");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password changed successfully for user: {}", user.getEmail());
    }
    
    /**
     * Upload user avatar
     * 
     * @param userId the user ID
     * @param file the avatar image file
     * @return updated user DTO
     * @throws ResourceNotFoundException if user not found
     * @throws ValidationException if file is invalid
     */
    @Transactional
    public UserDTO uploadAvatar(Long userId, MultipartFile file) {
        log.info("Uploading avatar for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Validate image file
        fileValidator.validateImage(file);
        
        // Delete old avatar if exists
        if (user.getAvatarUrl() != null) {
            try {
                s3Service.deleteFile(user.getAvatarUrl());
            } catch (Exception e) {
                log.warn("Failed to delete old avatar: {}", e.getMessage());
            }
        }
        
        // Upload new avatar
        String avatarUrl = s3Service.uploadFile(file, "avatars");
        user.setAvatarUrl(avatarUrl);
        
        user = userRepository.save(user);
        
        log.info("Avatar uploaded successfully for user: {}", user.getEmail());
        
        return convertToDTO(user);
    }
    
    /**
     * Convert User entity to DTO
     * 
     * @param user the user entity
     * @return user DTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

// Made with Bob
