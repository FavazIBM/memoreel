package com.memoreel.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String googleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Made with Bob
