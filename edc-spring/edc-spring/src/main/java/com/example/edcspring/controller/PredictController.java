package com.example.edcspring.controller;
import com.example.edcspring.entity.PredictAOP;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class PredictController {
    @Autowired
    private edcMapper edcMapper;
    @GetMapping("/getPredictAOP")
    public List<PredictAOP> getPredictAOP() {
        return edcMapper.getPredictAOP();
    }
    @GetMapping("/PredictDX")
    public Map<String, String> processString(@RequestParam String input) {
        // 调用 .exe 文件
        System.out.println(input);
        String result = callExeFile(input);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return response;
    }

    private String callExeFile(String input) {
        try {
            String exePath = "scripts/model/DX/DX.exe";
            // 设置工作目录
            File workingDirectory = new File("scripts/model/DX/");
            ProcessBuilder pb = new ProcessBuilder(exePath, input);
            pb.directory(workingDirectory); // 设置工作目录
            pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
            pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
            Process process = pb.start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return "Error: Process exited with code " + exitCode;
            }
            return "Process completed successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
    }


}
