package com.example.edcspring.controller;
import com.example.edcspring.entity.PredictAOP;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

            // 定义结果文件夹路径
            String jsonFilePath = "scripts/model/DX/" + input; // 结果文件夹路径
            // 检查文件夹是否存在
            File resultFolder = new File(jsonFilePath);
            if (resultFolder.exists() && resultFolder.isDirectory()) {
                // 如果文件夹存在，直接读取结果
                return readJsonFromFolder(jsonFilePath); // 按照文件夹读取结果
            }
            // 如果文件夹不存在，调用 exe 文件
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
//            String jsonFilePath = "scripts/model/DX/qualitative_model_runs.json"; // 假设生成的文件名为 output.json
//            return readJsonFile(jsonFilePath);//按照json文件读结果
            return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
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
    private String readJsonFromFolder(String folderPath) {//按文件夹读取结果文件
        File folder = new File(folderPath);
        StringBuilder jsonContent = new StringBuilder();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json")); // 过滤出 JSON 文件
            if (files != null && files.length > 0) {
                // 假设只处理第一个找到的 JSON 文件
                try (BufferedReader br = new BufferedReader(new FileReader(files[0]))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        jsonContent.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error reading JSON file: " + e.getMessage();
                }
            } else {
                return "No JSON files found in the folder.";
            }
        } else {
            return "Folder does not exist or is not a directory.";
        }
        return jsonContent.toString(); // 返回 JSON 内容
    }
    @GetMapping("/downloadPredict")
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam String PreType,String smiles,String fileName) {
//        String folderPath = "scripts/model/DX/output"; // 假设生成的文件夹名为 output
        String folderPath = "scripts/model/"+PreType+"/"+smiles; // 真正的下载路径
        File file = new File(folderPath, fileName);
        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
