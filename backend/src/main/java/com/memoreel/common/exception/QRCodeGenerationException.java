package com.memoreel.common.exception;

public class QRCodeGenerationException extends RuntimeException {
    
    public QRCodeGenerationException(String message) {
        super(message);
    }
    
    public QRCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Made with Bob
