package com.memoreel.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {
    private final String token;
    private final AuthUserDto user;
}

// Made with Bob
