package com.memoreel.project.repository;

import com.memoreel.project.entity.Project;
import com.memoreel.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    Page<Project> findByUserId(Long userId, Pageable pageable);
    
    Page<Project> findByUserIdAndStatus(Long userId, ProjectStatus status, Pageable pageable);
    
    List<Project> findByStatus(ProjectStatus status);
    
    @Query("SELECT p FROM Project p WHERE p.user.id = :userId AND p.id = :projectId")
    Optional<Project> findByIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.user.id = :userId AND p.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ProjectStatus status);
}

// Made with Bob
