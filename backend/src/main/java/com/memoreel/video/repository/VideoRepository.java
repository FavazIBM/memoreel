package com.memoreel.video.repository;

import com.memoreel.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    
    Optional<Video> findByProjectId(Long projectId);
    
    @Query("SELECT v FROM Video v WHERE v.project.id = :projectId AND v.project.user.id = :userId")
    Optional<Video> findByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
    
    boolean existsByProjectId(Long projectId);
    
    void deleteByProjectId(Long projectId);
}

// Made with Bob
