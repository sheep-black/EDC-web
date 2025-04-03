package com.example.edcspring.entity;

import java.util.Map;

/**
 * 任务状态实体类
 */
public class TaskStatus {
    private String taskId;           // 任务ID
    private String status;           // 状态：processing, completed, failed
    private int progress;            // 进度：0-100
    private String message;          // 状态消息
    private String error;            // 错误信息
    private String input;            // 输入信息
    private String ifAD;             // AD参数
    private Map<String, String> result;  // 处理结果

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getIfAD() {
        return ifAD;
    }

    public void setIfAD(String ifAD) {
        this.ifAD = ifAD;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}

