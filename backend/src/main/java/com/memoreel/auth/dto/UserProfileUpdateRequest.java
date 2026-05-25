package com.memoreel.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {
    @Email(message = "Email must be valid")
    private String email;

    private String avatarUrl;
    private String fullName;
}

// Made with Bob
