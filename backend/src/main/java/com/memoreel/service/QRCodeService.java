package com.memoreel.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.memoreel.common.exception.QRCodeGenerationException;
import com.memoreel.common.exception.ResourceNotFoundException;
import com.memoreel.common.exception.UnauthorizedException;
import com.memoreel.project.entity.Project;
import com.memoreel.project.repository.ProjectRepository;
import com.memoreel.qr.dto.QRCodeDTO;
import com.memoreel.qr.entity.QRCode;
import com.memoreel.qr.repository.QRCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * QR code service
 * Handles QR code generation and management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QRCodeService {
    
    private final QRCodeRepository qrCodeRepository;
    private final ProjectRepository projectRepository;
    private final S3Service s3Service;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    private static final int QR_CODE_SIZE = 300;
    private static final String QR_CODE_FORMAT = "PNG";
    
    /**
     * Generate QR code for project
     * 
     * @param project the project
     * @param publicUrl the public URL
     * @return QR code URL
     * @throws QRCodeGenerationException if generation fails
     */
    @Transactional
    public String generateQRCode(Project project, String publicUrl) {
        log.info("Generating QR code for project ID: {}", project.getId());
        
        try {
            // Generate QR code image
            byte[] qrCodeBytes = generateQRCodeImage(publicUrl, QR_CODE_SIZE);
            
            // Create multipart file for upload
            MockMultipartFile qrCodeFile = new MockMultipartFile(
                    "qrcode",
                    "qrcode-" + project.getId() + ".png",
                    "image/png",
                    qrCodeBytes
            );
            
            // Upload to S3
            String qrCodeUrl = s3Service.uploadFile(qrCodeFile, "qrcodes");
            
            // Save QR code entity
            QRCode qrCode = QRCode.builder()
                    .project(project)
                    .qrCodeUrl(qrCodeUrl)
                    .publicUrl(publicUrl)
                    .size(QR_CODE_SIZE)
                    .format(QR_CODE_FORMAT)
                    .build();
            
            qrCodeRepository.save(qrCode);
            
            log.info("QR code generated successfully for project: {}", project.getId());
            
            return qrCodeUrl;
            
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code for project: {}", project.getId(), e);
            throw new QRCodeGenerationException("Failed to generate QR code: " + e.getMessage());
        }
    }
    
    /**
     * Get QR code for project
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @return QR code DTO
     * @throws ResourceNotFoundException if QR code not found
     */
    @Transactional(readOnly = true)
    public QRCodeDTO getQRCode(Long userId, Long projectId) {
        log.info("Fetching QR code for project ID: {} by user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateProjectOwnership(project, userId);
        
        QRCode qrCode = qrCodeRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("QR code not found for this project"));
        
        String downloadUrl = baseUrl + "/api/v1/qr/" + project.getSlug() + "/download";
        
        return QRCodeDTO.builder()
                .qrCodeUrl(qrCode.getQrCodeUrl())
                .publicUrl(qrCode.getPublicUrl())
                .downloadUrl(downloadUrl)
                .size(qrCode.getSize())
                .format(qrCode.getFormat())
                .build();
    }
    
    /**
     * Get QR code image bytes by slug
     * 
     * @param slug the project slug
     * @return QR code image bytes
     * @throws ResourceNotFoundException if QR code not found
     */
    @Transactional(readOnly = true)
    public byte[] getQRCodeImageBySlug(String slug) {
        log.info("Fetching QR code image for slug: {}", slug);
        
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        QRCode qrCode = qrCodeRepository.findByProjectId(project.getId())
                .orElseThrow(() -> new ResourceNotFoundException("QR code not found"));
        
        try {
            // Regenerate QR code image for download
            return generateQRCodeImage(qrCode.getPublicUrl(), qrCode.getSize());
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code image for download", e);
            throw new QRCodeGenerationException("Failed to generate QR code image: " + e.getMessage());
        }
    }
    
    /**
     * Regenerate QR code for project
     * 
     * @param userId the user ID
     * @param projectId the project ID
     * @return QR code DTO
     */
    @Transactional
    public QRCodeDTO regenerateQRCode(Long userId, Long projectId) {
        log.info("Regenerating QR code for project ID: {} by user ID: {}", projectId, userId);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        validateProjectOwnership(project, userId);
        
        // Get existing QR code
        QRCode existingQrCode = qrCodeRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("QR code not found"));
        
        try {
            // Delete old QR code from S3
            s3Service.deleteFile(existingQrCode.getQrCodeUrl());
            
            // Generate new QR code image
            byte[] qrCodeBytes = generateQRCodeImage(existingQrCode.getPublicUrl(), QR_CODE_SIZE);
            
            // Create multipart file for upload
            MockMultipartFile qrCodeFile = new MockMultipartFile(
                    "qrcode",
                    "qrcode-" + project.getId() + ".png",
                    "image/png",
                    qrCodeBytes
            );
            
            // Upload to S3
            String newQrCodeUrl = s3Service.uploadFile(qrCodeFile, "qrcodes");
            
            // Update QR code entity
            existingQrCode.setQrCodeUrl(newQrCodeUrl);
            qrCodeRepository.save(existingQrCode);
            
            log.info("QR code regenerated successfully for project: {}", project.getId());
            
            String downloadUrl = baseUrl + "/api/v1/qr/" + project.getSlug() + "/download";
            
            return QRCodeDTO.builder()
                    .qrCodeUrl(newQrCodeUrl)
                    .publicUrl(existingQrCode.getPublicUrl())
                    .downloadUrl(downloadUrl)
                    .size(existingQrCode.getSize())
                    .format(existingQrCode.getFormat())
                    .build();
            
        } catch (WriterException | IOException e) {
            log.error("Failed to regenerate QR code for project: {}", project.getId(), e);
            throw new QRCodeGenerationException("Failed to regenerate QR code: " + e.getMessage());
        }
    }
    
    /**
     * Generate QR code image bytes
     * 
     * @param content the content to encode
     * @param size the QR code size
     * @return QR code image bytes
     * @throws WriterException if QR code generation fails
     * @throws IOException if image conversion fails
     */
    private byte[] generateQRCodeImage(String content, int size) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size);
        
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, QR_CODE_FORMAT, baos);
        
        return baos.toByteArray();
    }
    
    /**
     * Validate project ownership
     * 
     * @param project the project
     * @param userId the user ID
     */
    private void validateProjectOwnership(Project project, Long userId) {
        if (!project.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to access this project");
        }
    }
}

// Made with Bob
