package com.arpan.codeinsight.repository;

import com.arpan.codeinsight.model.ProjectFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFileEntity, Long> {
    List<ProjectFileEntity> findByProjectIdAndFileType(Long projectId, String fileType);

}
