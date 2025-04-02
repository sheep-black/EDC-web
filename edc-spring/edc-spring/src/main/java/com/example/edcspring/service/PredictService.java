package com.example.edcspring.service;

import com.example.edcspring.entity.PredictResult;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PredictService {
    List<PredictResult> getAllPredictResults();

    CompletableFuture<Map<String, String>> predictDX(String input, String ifAD);

    CompletableFuture<Map<String, String>> predictDL(String input, String ifAD);

    ResponseEntity<FileSystemResource> downloadPredictFile(String preType, String smiles, String fileName);

    ResponseEntity<ByteArrayResource> getEventImage(String preType, String smiles, String id);
}
