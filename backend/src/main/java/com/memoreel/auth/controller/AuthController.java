package com.memoreel.auth.controller;

import com.memoreel.api.ApiResponse;
import com.memoreel.auth.dto.AuthResponseDto;
import com.memoreel.auth.dto.GoogleAuthRequest;
import com.memoreel.auth.dto.LoginRequest;
import com.memoreel.auth.dto.RegisterRequest;
import com.memoreel.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponseDto> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request), "Registration successful");
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), "Login successful");
    }

    @PostMapping("/google")
    public ApiResponse<AuthResponseDto> google(@Valid @RequestBody GoogleAuthRequest request) {
        return ApiResponse.success(authService.googleLogin(request), "Google login successful");
    }
}

// Made with Bob
