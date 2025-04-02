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
    public CompletableFuture<Map<String, String>> processStringDX(@RequestParam String input, @RequestParam String ifAD) {
        // 记录收到的请求参数
        System.out.println("收到预测请求 - 输入: " + input + ", ifAD: " + ifAD);

        // 获取当前认证用户信息(在主线程中)，打印更多细节
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("主线程认证信息: " + (auth != null ? auth.getName() : "无认证信息"));
        if (auth != null) {
            System.out.println("主线程权限: " + auth.getAuthorities());
            System.out.println("认证详情: " + auth.getDetails());
            System.out.println("认证类型: " + auth.getClass().getName());
        }

        // 调用异步服务并添加回调以记录结果
        return predictService.predictDX(input, ifAD)
                .thenApply(result -> {
                    System.out.println("预测完成，结果: " + result);
                    return result;
                })
                .exceptionally(ex -> {
                    System.err.println("预测过程发生错误: " + ex.getMessage());
                    ex.printStackTrace();
                    throw new CompletionException(ex);
                });
    }

    @GetMapping("/PredictDL")
    public CompletableFuture<Map<String, String>> processStringDL(@RequestParam String input, @RequestParam String ifAD) {
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
}

