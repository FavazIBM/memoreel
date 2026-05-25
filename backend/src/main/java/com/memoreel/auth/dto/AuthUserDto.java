package com.memoreel.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUserDto {
    private final Long id;
    private final String email;
    private final String fullName;
    private final String avatarUrl;
}

// Made with Bob
