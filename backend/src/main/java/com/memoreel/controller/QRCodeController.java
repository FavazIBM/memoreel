package com.memoreel.controller;

import com.memoreel.common.dto.ApiResponse;
import com.memoreel.qr.dto.QRCodeDTO;
import com.memoreel.security.SecurityUser;
import com.memoreel.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * QR code controller
 * Handles QR code retrieval and regeneration
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QRCodeController {
    
    private final QRCodeService qrCodeService;
    
    /**
     * Get QR code for project
     * 
     * @param securityUser authenticated user
     * @param projectId project ID
     * @return QR code DTO
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<QRCodeDTO>> getQRCode(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long projectId) {
        log.info("Get QR code request for project ID: {} by user ID: {}", 
                projectId, securityUser.getId());
        
        QRCodeDTO qrCodeDTO = qrCodeService.getQRCode(securityUser.getId(), projectId);
        
        ApiResponse<QRCodeDTO> response = ApiResponse.<QRCodeDTO>builder()
                .success(true)
                .message("QR code retrieved successfully")
                .data(qrCodeDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Download QR code image by slug
     * No authentication required
     * 
     * @param slug project slug
     * @return QR code image bytes
     */
    @GetMapping("/{slug}/download")
    public ResponseEntity<byte[]> downloadQRCode(@PathVariable String slug) {
        log.info("Download QR code request for slug: {}", slug);
        
        byte[] qrCodeImage = qrCodeService.getQRCodeImageBySlug(slug);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "qrcode-" + slug + ".png");
        
        return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
    }
    
    /**
     * Regenerate QR code for project
     * 
     * @param securityUser authenticated user
     * @param projectId project ID
     * @return QR code DTO
     */
    @PostMapping("/regenerate/{projectId}")
    public ResponseEntity<ApiResponse<QRCodeDTO>> regenerateQRCode(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long projectId) {
        log.info("Regenerate QR code request for project ID: {} by user ID: {}", 
                projectId, securityUser.getId());
        
        QRCodeDTO qrCodeDTO = qrCodeService.regenerateQRCode(securityUser.getId(), projectId);
        
        ApiResponse<QRCodeDTO> response = ApiResponse.<QRCodeDTO>builder()
                .success(true)
                .message("QR code regenerated successfully")
                .data(qrCodeDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
