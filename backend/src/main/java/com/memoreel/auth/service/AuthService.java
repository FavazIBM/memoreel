package com.memoreel.auth.service;

import com.memoreel.auth.dto.AuthResponseDto;
import com.memoreel.auth.dto.AuthUserDto;
import com.memoreel.auth.dto.GoogleAuthRequest;
import com.memoreel.auth.dto.LoginRequest;
import com.memoreel.auth.dto.PasswordChangeRequest;
import com.memoreel.auth.dto.RegisterRequest;
import com.memoreel.auth.dto.UserProfileUpdateRequest;
import com.memoreel.auth.model.UserRecord;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthService {

    private final Map<Long, UserRecord> usersById = new LinkedHashMap<>();
    private final Map<String, UserRecord> usersByEmail = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public AuthResponseDto register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (usersByEmail.containsKey(normalizedEmail)) {
            throw new IllegalStateException("Email is already registered");
        }

        UserRecord user = UserRecord.builder()
                .id(sequence.getAndIncrement())
                .email(normalizedEmail)
                .password(request.getPassword())
                .fullName(request.getFullName() == null || request.getFullName().isBlank() ? "Memoreel User" : request.getFullName())
                .avatarUrl(null)
                .build();

        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail(), user);

        return buildAuthResponse(user, "mock-jwt-token-" + user.getId());
    }

    public AuthResponseDto login(LoginRequest request) {
        UserRecord user = usersByEmail.get(normalizeEmail(request.getEmail()));
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return buildAuthResponse(user, "mock-jwt-token-" + user.getId());
    }

    public AuthResponseDto googleLogin(GoogleAuthRequest request) {
        String email = "googleuser" + Math.abs(request.getToken().hashCode()) + "@memoreel.app";
        UserRecord user = usersByEmail.get(email);
        if (user == null) {
            user = UserRecord.builder()
                    .id(sequence.getAndIncrement())
                    .email(email)
                    .password("google-oauth")
                    .fullName("Google User")
                    .avatarUrl("https://ui-avatars.com/api/?name=Google+User")
                    .build();
            usersById.put(user.getId(), user);
            usersByEmail.put(user.getEmail(), user);
        }
        return buildAuthResponse(user, "mock-google-token-" + user.getId());
    }

    public AuthUserDto updateProfile(Long userId, UserProfileUpdateRequest request) {
        UserRecord user = requireUser(userId);

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String normalizedEmail = normalizeEmail(request.getEmail());
            UserRecord existing = usersByEmail.get(normalizedEmail);
            if (existing != null && !existing.getId().equals(userId)) {
                throw new IllegalStateException("Email is already registered");
            }
            usersByEmail.remove(user.getEmail());
            user.setEmail(normalizedEmail);
            usersByEmail.put(user.getEmail(), user);
        }

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }

        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        return toUserDto(user);
    }

    public void changePassword(Long userId, PasswordChangeRequest request) {
        UserRecord user = requireUser(userId);
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(request.getNewPassword());
    }

    public AuthUserDto getProfile(Long userId) {
        return toUserDto(requireUser(userId));
    }

    private UserRecord requireUser(Long userId) {
        UserRecord user = usersById.get(userId);
        if (user == null) {
            throw new SecurityException("User not found");
        }
        return user;
    }

    private AuthResponseDto buildAuthResponse(UserRecord user, String token) {
        return AuthResponseDto.builder()
                .token(token)
                .user(toUserDto(user))
                .build();
    }

    private AuthUserDto toUserDto(UserRecord user) {
        return AuthUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}

// Made with Bob
