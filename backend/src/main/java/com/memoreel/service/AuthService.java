package com.memoreel.service;

import com.memoreel.auth.dto.AuthResponse;
import com.memoreel.auth.dto.LoginRequest;
import com.memoreel.auth.dto.RegisterRequest;
import com.memoreel.auth.dto.TokenRefreshRequest;
import com.memoreel.auth.entity.User;
import com.memoreel.auth.repository.UserRepository;
import com.memoreel.common.exception.UnauthorizedException;
import com.memoreel.common.exception.ValidationException;
import com.memoreel.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service
 * Handles user registration, login, and token operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * Register a new user
     * 
     * @param request registration request
     * @return authentication response with tokens
     * @throws ValidationException if email already exists
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Email already registered");
        }
        
        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();
        
        user = userRepository.save(user);
        
        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        
        log.info("User registered successfully: {}", user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours in seconds
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
    
    /**
     * Login user
     * 
     * @param request login request
     * @return authentication response with tokens
     * @throws UnauthorizedException if credentials are invalid
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        
        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        
        log.info("User logged in successfully: {}", user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours in seconds
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
    
    /**
     * Google OAuth login
     * 
     * @param googleToken Google ID token
     * @return authentication response with tokens
     */
    @Transactional
    public AuthResponse googleLogin(String googleToken) {
        log.info("Google login attempt");
        
        // TODO: Verify Google token with Google API
        // For now, this is a placeholder implementation
        // In production, you would:
        // 1. Verify the token with Google
        // 2. Extract user info (email, name, picture)
        // 3. Create or update user
        
        throw new UnsupportedOperationException("Google login not yet implemented");
    }
    
    /**
     * Refresh access token
     * 
     * @param request token refresh request
     * @return authentication response with new access token
     * @throws UnauthorizedException if refresh token is invalid
     */
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        log.info("Token refresh attempt");
        
        String refreshToken = request.getRefreshToken();
        
        // Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        
        // Get user from token
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        // Generate new access token
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        
        log.info("Token refreshed successfully for user: {}", user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Return same refresh token
                .tokenType("Bearer")
                .expiresIn(86400L) // 24 hours in seconds
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}

// Made with Bob
