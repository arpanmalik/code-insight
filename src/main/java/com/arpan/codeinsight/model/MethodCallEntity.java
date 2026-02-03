package com.arpan.codeinsight.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "method_calls")
public class MethodCallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    // From (caller)
    private String callerClass;
    private String callerMethod;

    // To (callee)
    private String calleeClass;
    private String calleeMethod;

    // CONTROLLER -> SERVICE -> REPOSITORY
    private String callType;

    private LocalDateTime createdAt;

    public MethodCallEntity(){}

    public MethodCallEntity(Long id, Long projectId, String callerClass, String callerMethod, String calleeClass, String calleeMethod, String callType, LocalDateTime createdAt) {
        this.id = id;
        this.projectId = projectId;
        this.callerClass = callerClass;
        this.callerMethod = callerMethod;
        this.calleeClass = calleeClass;
        this.calleeMethod = calleeMethod;
        this.callType = callType;
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

    public String getCallerClass() {
        return callerClass;
    }

    public void setCallerClass(String callerClass) {
        this.callerClass = callerClass;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(String callerMethod) {
        this.callerMethod = callerMethod;
    }

    public String getCalleeClass() {
        return calleeClass;
    }

    public void setCalleeClass(String calleeClass) {
        this.calleeClass = calleeClass;
    }

    public String getCalleeMethod() {
        return calleeMethod;
    }

    public void setCalleeMethod(String calleeMethod) {
        this.calleeMethod = calleeMethod;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
