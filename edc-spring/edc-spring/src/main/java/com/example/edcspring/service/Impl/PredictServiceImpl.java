package com.example.edcspring.service.Impl;

import com.example.edcspring.entity.PredictResult;
import com.example.edcspring.mapper.edcMapper;
import com.example.edcspring.service.PredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class PredictServiceImpl implements PredictService {
    @Autowired
    private edcMapper edcMapper;

    private static final String BASE_SCRIPT_PATH = "scripts/edc_backend_models_v3/edc_backend_models_v3";
    private static final String RESULT_PATH = BASE_SCRIPT_PATH + "/result/";
    private static final String OUTPUT_LOG = "output.log";
    private static final String ERROR_LOG = "error.log";

    @Override
    public List<PredictResult> getAllPredictResults() {
        return edcMapper.getPredictResult();
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Map<String, String>> predictDX(String input, String ifAD) {
        // 获取当前线程的SecurityContext (不需要再获取一次，因为已经配置了委托执行器)
        Map<String, String> response = new HashMap<>();

        try {
            // 打印更详细的认证信息，包括权限
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("异步线程认证信息: " + (auth != null ? auth.getName() : "无认证信息"));
            if (auth != null) {
                System.out.println("异步线程权限: " + auth.getAuthorities());
            }

            String result = callExeFile(input, ifAD, "DX", "qualitative_model");
            response.put("result", result);
            response.put("ifAD", ifAD);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "处理请求时出错: " + e.getMessage());
            return CompletableFuture.completedFuture(response);
        }
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Map<String, String>> predictDL(String input, String ifAD) {
        Map<String, String> response = new HashMap<>();
        try {
            String result = callExeFile(input, ifAD, "DL", "quantitative_model");
            response.put("result", result);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error processing request: " + e.getMessage());
            return CompletableFuture.completedFuture(response);
        }
    }

    private String callExeFile(String input, String ifAD, String modelType, String modelFolder) {
        try {
            String exePath = BASE_SCRIPT_PATH + "/" + modelFolder + "/model.exe";
            File workingDirectory = new File(BASE_SCRIPT_PATH + "/" + modelFolder + "/");

            // 查询或创建记录
            PredictResult record = getOrCreateRecord(input);
            String jsonFilePath = RESULT_PATH + record.getId() + "/" + modelType;
            String outFilePath = "../result/" + record.getId() + "/" + modelType;

            // 检查是否需要运行模型
            if (shouldRunModel(jsonFilePath, ifAD)) {
                runModel(exePath, workingDirectory, input, outFilePath, ifAD);
            }

            // 读取生成的JSON文件
            return readJsonFromFolder(jsonFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
    }

    // 其余方法保持不变...

    // 以下是您原有的方法，保持不变
    private PredictResult getOrCreateRecord(String input) {
        // 方法实现保持不变
        PredictResult existingRecord = edcMapper.findBySmile(input);
        if (existingRecord != null) {
            return existingRecord;
        }

        // 如果记录不存在，新增记录
        PredictResult newRecord = new PredictResult();
        newRecord.setSource(input);
        edcMapper.insertPredictResult(newRecord);
        return newRecord;
    }

    private boolean shouldRunModel(String jsonFilePath, String ifAD) {
        // 方法实现保持不变
        File resultFolder = new File(jsonFilePath);

        // 如果结果文件夹不存在，需要运行模型
        if (!resultFolder.exists() || !resultFolder.isDirectory()) {
            return true;
        }

        String runJsonPath = jsonFilePath + "/runs.json";
        File runJsonFile = new File(runJsonPath);

        // 如果runs.json不存在，需要运行模型
        if (!runJsonFile.exists() || !runJsonFile.isFile()) {
            return true;
        }

        // 如果请求AD但AD文件夹为空，需要运行模型
        if ("AD".equals(ifAD)) {
            String adPath = jsonFilePath + "/AD";
            File adFolder = new File(adPath);
            String[] files = adFolder.list();
            if (files == null || files.length == 0) {
                return true;
            }
        }

        return false;
    }

    private void runModel(String exePath, File workingDirectory, String input, String outFilePath, String ifAD) throws IOException, InterruptedException {
        // 方法实现保持不变
        List<String> command = new ArrayList<>();
        command.add(exePath);
        command.add("-i");
        command.add(input);
        command.add("-o");
        command.add(outFilePath);

        // 根据ifAD的值添加"-s"参数
        if ("AD".equals(ifAD)) {
            command.add("-s");
        }

        // 调用exe文件
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDirectory);
        pb.redirectOutput(new File(OUTPUT_LOG));
        pb.redirectError(new File(ERROR_LOG));

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Process exited with code " + exitCode);
        }
    }

    private String readJsonFromFolder(String folderPath) {
        // 方法实现保持不变
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return "Folder does not exist or is not a directory.";
        }

        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            return "No JSON files found in the folder.";
        }

        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFiles[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            return jsonContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading JSON file: " + e.getMessage();
        }
    }

    @Override
    public ResponseEntity<FileSystemResource> downloadPredictFile(String preType, String smiles, String fileName) {
        // 方法实现保持不变
        PredictResult record = edcMapper.findBySmile(smiles);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        String filePath = RESULT_PATH + record.getId() + "/" + preType + "/" + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ByteArrayResource> getEventImage(String preType, String smiles, String id) {
        // 方法实现保持不变
        try {
            PredictResult record = edcMapper.findBySmile(smiles);
            if (record == null) {
                return ResponseEntity.notFound().build();
            }

            String imagePath = RESULT_PATH + record.getId() + "/" + preType + "/AD/" + id + ".png";
            File imageFile = new File(imagePath);

            if (!imageFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            ByteArrayResource resource = new ByteArrayResource(imageBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
