package com.example.edcspring.service;

import com.example.edcspring.entity.PredictResult;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface PredictService {
    List<PredictResult> getAllPredictResults();

    // 同步接口 - 保持不变
    Map<String, String> predictDX(String input, String ifAD);

    // 异步接口 - 返回类型改为Map<String, Object>包含taskId
    Map<String, Object> predictDL(String input, String ifAD);

    // 新增任务状态查询接口
    Map<String, Object> getTaskStatus(String taskId);

    ResponseEntity<FileSystemResource> downloadPredictFile(String preType, String smiles, String fileName);

    ResponseEntity<ByteArrayResource> getEventImage(String preType, String smiles, String id);
}

