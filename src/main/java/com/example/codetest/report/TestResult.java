package com.example.codetest.report;

public class TestResult {
    private String className;
    private String methodName;
    private boolean passed;
    private String errorMessage;
    private long executionTimeMs;
    private double cpuUsagePercent;
    private double memoryUsageMb;

    public TestResult(String className, String methodName, boolean passed, String errorMessage, long executionTimeMs, double cpuUsagePercent, double memoryUsageMb) {
        this.className = className;
        this.methodName = methodName;
        this.passed = passed;
        this.errorMessage = errorMessage;
        this.executionTimeMs = executionTimeMs;
        this.cpuUsagePercent = cpuUsagePercent;
        this.memoryUsageMb = memoryUsageMb;
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

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public double getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    public void setCpuUsagePercent(double cpuUsagePercent) {
        this.cpuUsagePercent = cpuUsagePercent;
    }

    public double getMemoryUsageMb() {
        return memoryUsageMb;
    }

    public void setMemoryUsageMb(double memoryUsageMb) {
        this.memoryUsageMb = memoryUsageMb;
    }
}