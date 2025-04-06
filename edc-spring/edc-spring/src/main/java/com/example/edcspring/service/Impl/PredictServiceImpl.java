package com.example.edcspring.service.Impl;

import com.example.edcspring.entity.PredictResult;
import com.example.edcspring.entity.TaskStatus;
import com.example.edcspring.mapper.edcMapper;
import com.example.edcspring.entity.ADGenerationTask;
import com.example.edcspring.entity.ADGenerationResult;
import com.example.edcspring.service.PredictService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PredictServiceImpl implements PredictService {
    @Autowired
    private edcMapper edcMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String BASE_SCRIPT_PATH = "scripts/edc_backend_models_v3/edc_backend_models_v3";
    private static final String RESULT_PATH = BASE_SCRIPT_PATH + "/result/";
    private static final String OUTPUT_LOG = "output.log";
    private static final String ERROR_LOG = "error.log";

    // RabbitMQ 队列名称
    private static final String AD_TASK_QUEUE = "ad-generation-tasks";
    private static final String AD_RESULT_QUEUE = "ad-generation-results";

    private final Map<String, CompletableFuture<String>> ongoingTasks = new ConcurrentHashMap<>();
    // 创建自定义线程池，用于执行模型调用
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    // 任务状态存储 - 生产环境可考虑使用Redis或数据库
    private final Map<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();

    @Override
    public List<PredictResult> getAllPredictResults() {
        return edcMapper.getPredictResult();
    }

    @Override
    public Map<String, String> predictDX(String input, String ifAD) {
        Map<String, String> response = new HashMap<>();
        try {
            // 使用异步方式调用exe文件
            String result = callExeFileAsync(input, ifAD, "DX", "qualitative_model").get();
            response.put("result", result);
            response.put("ifAD", ifAD);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error processing request: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Map<String, Object> predictDL(String input, String ifAD) {
        // 生成唯一任务ID
        String taskId = UUID.randomUUID().toString();

        // 创建任务状态对象
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setTaskId(taskId);
        taskStatus.setStatus("processing");
        taskStatus.setProgress(0);
        taskStatus.setInput(input);
        taskStatus.setIfAD(ifAD);

        // 存储任务状态
        taskStatusMap.put(taskId, taskStatus);

        // 异步提交任务，不等待结果
        CompletableFuture.runAsync(() -> {
            try {
                // 更新进度 - 开始处理
                updateTaskProgress(taskId, 10, "开始处理任务");

                // 异步调用模型
                String result = callExeFileAsync(input, ifAD, "DL", "quantitative_model").get();

                // 更新进度 - 处理完成
                updateTaskProgress(taskId, 100, "处理完成");

                // 存储结果
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("result", result);
                resultMap.put("ifAD", ifAD);
                taskStatus.setResult(resultMap);
                taskStatus.setStatus("completed");
            } catch (Exception e) {
                e.printStackTrace();
                taskStatus.setStatus("failed");
                taskStatus.setError("Error processing request: " + e.getMessage());
            }
        }, executorService);

        // 立即返回任务信息
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", taskId);
        response.put("status", "processing");
        response.put("message", "任务已提交，请通过taskId查询结果");
        return response;
    }

    /**
     * 获取任务状态
     */
    @Override
    public Map<String, Object> getTaskStatus(String taskId) {
        TaskStatus taskStatus = taskStatusMap.get(taskId);
        Map<String, Object> response = new HashMap<>();

        if (taskStatus == null) {
            response.put("status", "not_found");
            response.put("message", "未找到指定的任务ID");
            return response;
        }

        response.put("taskId", taskId);
        response.put("status", taskStatus.getStatus());
        response.put("progress", taskStatus.getProgress());
        response.put("message", taskStatus.getMessage());

        // 如果任务完成，返回结果
        if ("completed".equals(taskStatus.getStatus())) {
            response.put("result", taskStatus.getResult());

            // 可选：任务完成一段时间后清理内存
            scheduleTaskCleanup(taskId);
        } else if ("failed".equals(taskStatus.getStatus())) {
            response.put("error", taskStatus.getError());
        }

        return response;
    }

    /**
     * 更新任务进度
     */
    private void updateTaskProgress(String taskId, int progress, String message) {
        TaskStatus taskStatus = taskStatusMap.get(taskId);
        if (taskStatus != null) {
            taskStatus.setProgress(progress);
            taskStatus.setMessage(message);
        }
    }

    /**
     * 计划清理完成的任务状态（防止内存泄漏）
     */
    private void scheduleTaskCleanup(String taskId) {
        CompletableFuture.runAsync(() -> {
            try {
                // 任务完成后30分钟清理
                Thread.sleep(30 * 60 * 1000);
                taskStatusMap.remove(taskId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, executorService);
    }

    /**
     * 将任务发送到RabbitMQ队列
     */
    private CompletableFuture<String> sendToRabbitMQ(String input, String ifAD, String modelType, String modelFolder, String taskKey) {
        CompletableFuture<String> future = new CompletableFuture<>();

        try {
            // 查询或创建记录
            PredictResult record;
            synchronized (this) {
                record = getOrCreateRecord(input);
            }

            String jsonFilePath = RESULT_PATH + record.getId() + "/" + modelType;
            String outFilePath = "../result/" + record.getId() + "/" + modelType;

            // 如果不需要运行模型（结果已存在），直接返回结果
            if (!shouldRunModel(jsonFilePath, ifAD)) {
                String result = readJsonFromFolder(jsonFilePath);
                future.complete(result);
                return future;
            }

            // 创建AD生成任务
            ADGenerationTask task = new ADGenerationTask();
            task.setTaskId(taskKey);
            task.setInput(input);
            task.setModelType(modelType);
            task.setModelFolder(modelFolder);
            task.setRecordId(record.getId());
            task.setResultPath(outFilePath);
            task.setIfAD(ifAD);

            // 发送任务到RabbitMQ

            rabbitTemplate.convertAndSend(AD_TASK_QUEUE, task);

            // 存储Future以便结果回调时完成
            ongoingTasks.put(taskKey, future);

            // 设置超时处理
            scheduleTimeout(taskKey, future, 30);

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * 设置任务超时
     */
    private void scheduleTimeout(String taskKey, CompletableFuture<String> future, int timeoutMinutes) {
        CompletableFuture.runAsync(() -> {
            try {
                // 等待指定分钟
                Thread.sleep(timeoutMinutes * 60 * 1000);

                // 如果任务仍未完成，则标记为超时
                if (!future.isDone()) {
                    future.completeExceptionally(
                            new TimeoutException("任务处理超时，请稍后重试")
                    );
                    ongoingTasks.remove(taskKey);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, executorService);
    }

    // 异步版本的callExeFile方法（带去重机制和分布式处理）
    private CompletableFuture<String> callExeFileAsync(String input, String ifAD, String modelType, String modelFolder) {
        // 创建一个唯一键，结合多个参数
        String taskKey = input + "-" + ifAD + "-" + modelType;
        // 检查是否已有相同任务在执行，如有则复用，没有则创建新任务
        return ongoingTasks.computeIfAbsent(taskKey, k -> {
            // 判断是否为AD图生成任务
//          if ("AD".equals(ifAD) && ("DX".equals(modelType) || "DL".equals(modelType))) {AD才是正确的输入判断 改成true是为了判断失败 进而走单机预测
            if ("true".equals(ifAD) && ("DX".equals(modelType) || "DL".equals(modelType))) {
                // AD图生成任务发送到RabbitMQ
                return sendToRabbitMQ(input, ifAD, modelType, modelFolder, taskKey);
            } else {
                // 非AD图任务在本地处理
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        String exePath = BASE_SCRIPT_PATH + "/" + modelFolder + "/model.exe";
                        File workingDirectory = new File(BASE_SCRIPT_PATH + "/" + modelFolder + "/");

                        // 查询或创建记录 - 需要同步操作，避免并发问题
                        PredictResult record;
                        synchronized (this) {
                            record = getOrCreateRecord(input);
                        }

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
                    } finally {
                        // 设置延迟移除，避免短时间内的频繁请求仍然导致重复执行
                        CompletableFuture.runAsync(() -> {
                            try {
                                // 为了防止任务刚完成就被移除，然后立即又有新请求，保留一段时间
                                Thread.sleep(5000); // 5秒后移除
                                ongoingTasks.remove(taskKey);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }, executorService);
                    }
                }, executorService);

                return future;
            }
        });
    }

    /**
     * 处理从RabbitMQ返回的结果
     * 此方法将由RabbitMQ监听器调用
     */
    public void handleADGenerationResult(ADGenerationResult result) {
        String taskId = result.getTaskId();
        CompletableFuture<String> future = ongoingTasks.get(taskId);

        if (future != null) {
            if ("COMPLETED".equals(result.getStatus())) {
                try {
                    // 从结果路径读取JSON
                    String jsonResult = readJsonFromFolder(result.getResultPath());
                    future.complete(jsonResult);
                } catch (Exception e) {
                    future.completeExceptionally(new RuntimeException("无法读取结果: " + e.getMessage()));
                }
            } else {
                future.completeExceptionally(new RuntimeException(result.getErrorMessage()));
            }

            // 任务已处理，从映射中移除
            ongoingTasks.remove(taskId);
        }
    }

    // 原有的同步方法保留，但内部改为调用异步方法
    private String callExeFile(String input, String ifAD, String modelType, String modelFolder) {
        try {
            return callExeFileAsync(input, ifAD, modelType, modelFolder).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
    }

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
            if (!adFolder.exists() || !adFolder.isDirectory()) {
                return true;
            }
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

    // 添加线程池的优雅关闭方法
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // TimeoutException内部类
    private static class TimeoutException extends RuntimeException {
        public TimeoutException(String message) {
            super(message);
        }
    }
}

