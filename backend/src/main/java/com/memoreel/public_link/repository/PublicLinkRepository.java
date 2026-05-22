package com.memoreel.public_link.repository;

import com.memoreel.public_link.entity.PublicLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicLinkRepository extends JpaRepository<PublicLink, Long> {
    
    Optional<PublicLink> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    Optional<PublicLink> findByProjectId(Long projectId);
    
    @Query("SELECT pl FROM PublicLink pl WHERE pl.project.id = :projectId AND pl.project.user.id = :userId")
    Optional<PublicLink> findByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
    
    void deleteByProjectId(Long projectId);
}

// Made with Bob
