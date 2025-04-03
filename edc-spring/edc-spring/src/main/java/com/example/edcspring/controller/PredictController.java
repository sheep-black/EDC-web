package com.example.edcspring.controller;

import com.example.edcspring.entity.PredictResult;
import com.example.edcspring.service.PredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
public class PredictController {

    @Autowired
    private PredictService predictService;

    @GetMapping("/getPredictResult")
    public List<PredictResult> getPredictResult() {
        return predictService.getAllPredictResults();
    }

    @GetMapping("/PredictDX")
    public Map<String, String> processStringDX(@RequestParam String input, @RequestParam String ifAD) {
        return predictService.predictDX(input, ifAD);
    }

    @PostMapping("/PredictDL")
    public Map<String, Object> predictDL(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        String ifAD = request.get("ifAD");
        return predictService.predictDL(input, ifAD);
    }

    @GetMapping("/downloadPredict")
    public ResponseEntity<FileSystemResource> downloadFile(
            @RequestParam String PreType,
            @RequestParam String smiles,
            @RequestParam String fileName) {
        return predictService.downloadPredictFile(PreType, smiles, fileName);
    }

    @GetMapping("/getEventImage")
    public ResponseEntity<ByteArrayResource> getEventImage(
            @RequestParam String PreType,
            @RequestParam String smiles,
            @RequestParam String id) {
        return predictService.getEventImage(PreType, smiles, id);
    }

    @GetMapping("/status/{taskId}")
    public Map<String, Object> getTaskStatus(@PathVariable String taskId) {
        return predictService.getTaskStatus(taskId);
    }
}

