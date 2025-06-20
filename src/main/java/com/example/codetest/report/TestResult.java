package com.example.codetest.report;

public class TestResult {
    private String className;
    private String methodName;
    private boolean passed;
    private String errorMessage;

    public TestResult(String className, String methodName, boolean passed, String errorMessage) {
        this.className = className;
        this.methodName = methodName;
        this.passed = passed;
        this.errorMessage = errorMessage;
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
}
