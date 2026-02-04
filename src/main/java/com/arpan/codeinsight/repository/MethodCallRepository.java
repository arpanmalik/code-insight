package com.arpan.codeinsight.repository;

import com.arpan.codeinsight.model.MethodCallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MethodCallRepository extends JpaRepository<MethodCallEntity, Long> {

    List<MethodCallEntity> findByProjectId(Long projectId);

    List<MethodCallEntity> findByProjectIdAndCallerClassAndCallerMethod(
            Long projectId,
            String callerClass,
            String callerMethod
    );

}
