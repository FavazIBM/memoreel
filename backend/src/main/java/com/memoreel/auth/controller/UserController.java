package com.memoreel.auth.controller;

import com.memoreel.api.ApiResponse;
import com.memoreel.auth.dto.AuthUserDto;
import com.memoreel.auth.dto.PasswordChangeRequest;
import com.memoreel.auth.dto.UserProfileUpdateRequest;
import com.memoreel.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final AuthService authService;

    @GetMapping("/profile")
    public ApiResponse<AuthUserDto> getProfile(@RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId) {
        return ApiResponse.success(authService.getProfile(userId), "Profile fetched successfully");
    }

    @PutMapping("/profile")
    public ApiResponse<AuthUserDto> updateProfile(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {
        return ApiResponse.success(authService.updateProfile(userId, request), "Profile updated successfully");
    }

    @PutMapping("/password")
    public ApiResponse<String> changePassword(
            @RequestHeader(name = "X-User-Id", defaultValue = "1") Long userId,
            @Valid @RequestBody PasswordChangeRequest request
    ) {
        authService.changePassword(userId, request);
        return ApiResponse.success("OK", "Password changed successfully");
    }
}

// Made with Bob
