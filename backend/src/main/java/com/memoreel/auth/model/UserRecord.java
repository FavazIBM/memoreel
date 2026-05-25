package com.memoreel.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRecord {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String avatarUrl;
}

// Made with Bob
