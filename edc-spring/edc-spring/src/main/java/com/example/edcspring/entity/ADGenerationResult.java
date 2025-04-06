package com.example.edcspring.entity;

import java.io.Serializable;

/**
 * AD图生成结果消息类
 */
public class ADGenerationResult implements Serializable {
    private String taskId;          // 对应的任务ID
    private String status;          // 状态：COMPLETED/FAILED
    private String resultPath;      // 生成图的存储路径
    private String errorMessage;    // 错误信息（如有）

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

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

