package com.memoreel.qr.repository;

import com.memoreel.qr.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
    
    Optional<QRCode> findByProjectId(Long projectId);
    
    @Query("SELECT qr FROM QRCode qr WHERE qr.project.id = :projectId AND qr.project.user.id = :userId")
    Optional<QRCode> findByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
    
    void deleteByProjectId(Long projectId);
}

// Made with Bob
