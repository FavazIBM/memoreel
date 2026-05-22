package com.memoreel.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private boolean success;
    private String error;
    private String message;
    private String errorCode;
    private Integer status;
    private String path;
    private LocalDateTime timestamp;
    private List<ValidationError> validationErrors;
    private Map<String, Object> details;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
    
    public static ErrorResponse of(String error, String message, Integer status, String path) {
        return ErrorResponse.builder()
                .success(false)
                .error(error)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static ErrorResponse of(String error, String message, String errorCode, Integer status, String path) {
        return ErrorResponse.builder()
                .success(false)
                .error(error)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

// Made with Bob
