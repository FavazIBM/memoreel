package com.memoreel.controller;

import com.memoreel.auth.dto.*;
import com.memoreel.common.dto.ApiResponse;
import com.memoreel.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller
 * Handles user registration, login, and token operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Register a new user
     * 
     * @param request registration request
     * @return authentication response with tokens
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for email: {}", request.getEmail());
        
        AuthResponse authResponse = authService.register(request);
        
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(authResponse)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Login user
     * 
     * @param request login request
     * @return authentication response with tokens
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        
        AuthResponse authResponse = authService.login(request);
        
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Google OAuth login
     * 
     * @param googleToken Google ID token
     * @return authentication response with tokens
     */
    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> googleLogin(@RequestBody String googleToken) {
        log.info("Google login request");
        
        AuthResponse authResponse = authService.googleLogin(googleToken);
        
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Google login successful")
                .data(authResponse)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Refresh access token
     * 
     * @param request token refresh request
     * @return authentication response with new access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Token refresh request");
        
        AuthResponse authResponse = authService.refreshToken(request);
        
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(authResponse)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
