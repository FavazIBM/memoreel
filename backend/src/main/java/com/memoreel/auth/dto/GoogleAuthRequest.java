package com.memoreel.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleAuthRequest {
    @NotBlank(message = "Google token is required")
    private String token;
}

// Made with Bob
