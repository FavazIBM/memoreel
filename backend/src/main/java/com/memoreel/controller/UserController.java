package com.memoreel.controller;

import com.memoreel.auth.dto.ChangePasswordRequest;
import com.memoreel.auth.dto.UpdateProfileRequest;
import com.memoreel.auth.dto.UserDTO;
import com.memoreel.common.dto.ApiResponse;
import com.memoreel.security.SecurityUser;
import com.memoreel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * User controller
 * Handles user profile operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * Get current user profile
     * 
     * @param securityUser authenticated user
     * @return user DTO
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(@AuthenticationPrincipal SecurityUser securityUser) {
        log.info("Get profile request for user ID: {}", securityUser.getId());
        
        UserDTO userDTO = userService.getUserById(securityUser.getId());
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("Profile retrieved successfully")
                .data(userDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update user profile
     * 
     * @param securityUser authenticated user
     * @param request update profile request
     * @return updated user DTO
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @AuthenticationPrincipal SecurityUser securityUser,
            @Valid @RequestBody UpdateProfileRequest request) {
        log.info("Update profile request for user ID: {}", securityUser.getId());
        
        UserDTO userDTO = userService.updateProfile(securityUser.getId(), request);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(userDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Change user password
     * 
     * @param securityUser authenticated user
     * @param request change password request
     * @return success response
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal SecurityUser securityUser,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Change password request for user ID: {}", securityUser.getId());
        
        userService.changePassword(securityUser.getId(), request);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Upload user avatar
     * 
     * @param securityUser authenticated user
     * @param file avatar image file
     * @return updated user DTO
     */
    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<UserDTO>> uploadAvatar(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestParam("file") MultipartFile file) {
        log.info("Upload avatar request for user ID: {}", securityUser.getId());
        
        UserDTO userDTO = userService.uploadAvatar(securityUser.getId(), file);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("Avatar uploaded successfully")
                .data(userDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
