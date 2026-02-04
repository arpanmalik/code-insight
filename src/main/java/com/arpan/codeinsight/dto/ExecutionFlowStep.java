package com.arpan.codeinsight.dto;

public class ExecutionFlowStep {
    private int stepOrder;
    private String className;
    private String methodName;
    private String componentType;

    public ExecutionFlowStep(int stepOrder, String className, String methodName, String componentType) {
        this.stepOrder = stepOrder;
        this.className = className;
        this.methodName = methodName;
        this.componentType = componentType;
    }

    public int getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(int stepOrder) {
        this.stepOrder = stepOrder;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
}
