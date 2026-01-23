package com.arpan.codeinsight.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_components")
public class CodeComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private String componentType; // CONTROLLER / SERVICE / REPOSITORY

    private String className;

    private String filePath;

    private LocalDateTime createdAt;

    public CodeComponentEntity() {}

    public CodeComponentEntity(Long id, Long projectId, String componentType, String className, String filePath, LocalDateTime createdAt) {
        this.id = id;
        this.projectId = projectId;
        this.componentType = componentType;
        this.className = className;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
