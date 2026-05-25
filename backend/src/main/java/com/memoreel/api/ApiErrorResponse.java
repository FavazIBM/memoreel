package com.memoreel.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private final boolean success;
    private final ErrorBody error;

    public static ApiErrorResponse of(String code, String message) {
        return new ApiErrorResponse(false, new ErrorBody(code, message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorBody {
        private final String code;
        private final String message;
    }
}

// Made with Bob
