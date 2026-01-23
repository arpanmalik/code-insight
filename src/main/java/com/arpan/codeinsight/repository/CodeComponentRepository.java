package com.arpan.codeinsight.repository;

import com.arpan.codeinsight.model.CodeComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeComponentRepository extends JpaRepository<CodeComponentEntity, Long> {
}
