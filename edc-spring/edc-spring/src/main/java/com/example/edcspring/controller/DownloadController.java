package com.example.edcspring.controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;


@RestController
public class DownloadController {
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {
        System.out.println("文件名：");
        System.out.print(fileName);
        Resource resource = new ClassPathResource("files/" + fileName); //文件路径：src/main/resources/files
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}

