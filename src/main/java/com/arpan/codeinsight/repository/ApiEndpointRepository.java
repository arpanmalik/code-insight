package com.arpan.codeinsight.repository;

import com.arpan.codeinsight.model.ApiEndpointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiEndpointRepository extends JpaRepository<ApiEndpointEntity, Long> {
}
