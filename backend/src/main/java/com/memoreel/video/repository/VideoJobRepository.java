package com.memoreel.video.repository;

import com.memoreel.video.entity.VideoJob;
import com.memoreel.video.entity.VideoJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoJobRepository extends JpaRepository<VideoJob, Long> {
    
    List<VideoJob> findByProjectId(Long projectId);
    
    List<VideoJob> findByStatus(VideoJobStatus status);
    
    @Query("SELECT vj FROM VideoJob vj WHERE vj.project.id = :projectId ORDER BY vj.createdAt DESC")
    List<VideoJob> findByProjectIdOrderByCreatedAtDesc(@Param("projectId") Long projectId);
    
    @Query("SELECT vj FROM VideoJob vj WHERE vj.project.id = :projectId AND vj.status = :status ORDER BY vj.createdAt DESC")
    Optional<VideoJob> findLatestByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") VideoJobStatus status);
    
    @Query("SELECT vj FROM VideoJob vj WHERE vj.status IN :statuses AND vj.createdAt < :before")
    List<VideoJob> findStaleJobs(@Param("statuses") List<VideoJobStatus> statuses, @Param("before") LocalDateTime before);
    
    @Query("SELECT vj FROM VideoJob vj WHERE vj.project.id = :projectId AND vj.project.user.id = :userId")
    List<VideoJob> findByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
    
    void deleteByProjectId(Long projectId);
    
    // Additional methods for VideoJobScheduler
    List<VideoJob> findByStatusAndStartedAtBefore(VideoJobStatus status, LocalDateTime before);
    
    List<VideoJob> findByStatusAndCompletedAtBefore(VideoJobStatus status, LocalDateTime before);
    
    long countByStatus(VideoJobStatus status);
}

// Made with Bob
