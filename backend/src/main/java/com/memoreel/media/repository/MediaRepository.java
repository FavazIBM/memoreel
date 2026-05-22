package com.memoreel.media.repository;

import com.memoreel.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    List<Media> findByProjectIdOrderByOrderIndexAsc(Long projectId);
    
    @Query("SELECT m FROM Media m WHERE m.project.id = :projectId AND m.id = :mediaId")
    Optional<Media> findByIdAndProjectId(@Param("mediaId") Long mediaId, @Param("projectId") Long projectId);
    
    @Query("SELECT COUNT(m) FROM Media m WHERE m.project.id = :projectId")
    long countByProjectId(@Param("projectId") Long projectId);
    
    @Query("SELECT MAX(m.orderIndex) FROM Media m WHERE m.project.id = :projectId")
    Optional<Integer> findMaxOrderIndexByProjectId(@Param("projectId") Long projectId);
    
    @Query("SELECT m FROM Media m WHERE m.project.id = :projectId AND m.orderIndex = :orderIndex")
    Optional<Media> findByProjectIdAndOrderIndex(@Param("projectId") Long projectId, @Param("orderIndex") Integer orderIndex);
    
    void deleteByProjectId(Long projectId);
}

// Made with Bob
