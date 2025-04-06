package com.example.edcspring.entity;

import java.io.Serializable;

/**
 * AD图生成任务消息类
 */
public class ADGenerationTask implements Serializable {
    private String taskId;          // 任务唯一标识
    private String input;           // 输入数据
    private String modelType;       // DX或DL
    private String modelFolder;     // 模型文件夹
    private Long recordId;          // 关联的记录ID
    private String resultPath;      // 结果存储路径
    private String ifAD;            // 是否生成AD图

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelFolder() {
        return modelFolder;
    }

    public void setModelFolder(String modelFolder) {
        this.modelFolder = modelFolder;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = (long) recordId;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getIfAD() {
        return ifAD;
    }

    public void setIfAD(String ifAD) {
        this.ifAD = ifAD;
    }
}

