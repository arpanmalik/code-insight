package com.arpan.codeinsight.repository;

import com.arpan.codeinsight.model.MethodCallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodCallRepository extends JpaRepository<MethodCallEntity, Long> {
}
