package com.example.edcspring.controller;
import com.example.edcspring.entity.PredictAOP;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.*;

import java.io.*;
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
    public Map<String, String> processStringDX(@RequestParam String input) {
        // 调用 .exe 文件
        String result = callExeFileDX(input);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return response;
    }
    @GetMapping("/PredictDL")
    public Map<String, String> processStringDL(@RequestParam String input) {
        // 调用 .exe 文件
        String result = callExeFileDL(input);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return response;
    }
    private String callExeFileDX(String input) {
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
            // 读取生成的 JSON 文件
            String jsonFilePath = "scripts/model/DX/qualitative_model_runs.json"; // 假设生成的文件名为 output.json
            return readJsonFile(jsonFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
    }
    private String callExeFileDL(String input) {
        try {
            String exePath = "scripts/model/DL/DL.exe";
            // 设置工作目录
            File workingDirectory = new File("scripts/model/DL/");
            ProcessBuilder pb = new ProcessBuilder(exePath, input);
            pb.directory(workingDirectory); // 设置工作目录
            pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
            pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "Error: Process exited with code " + exitCode;
            }
            // 读取生成的 JSON 文件
            String jsonFilePath = "scripts/model/DL/qualitative_model_runs.json"; // 假设生成的文件名为 output.json
            return readJsonFile(jsonFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
    }
    private String readJsonFile(String filePath) {
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading JSON file: " + e.getMessage();
        }
        return jsonContent.toString(); // 返回 JSON 内容
    }


}
