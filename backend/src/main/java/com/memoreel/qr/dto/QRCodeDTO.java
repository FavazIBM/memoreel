package com.memoreel.qr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for QR code information
 * Contains QR code URL, public URL, download URL, size, and format
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeDTO {
    
    private String qrCodeUrl;
    
    private String publicUrl;
    
    private String downloadUrl;
    
    private Integer size;
    
    private String format;
}

// Made with Bob
