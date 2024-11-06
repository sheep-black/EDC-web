package com.example.edcspring.controller;
import com.example.edcspring.entity.PredictResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class PredictController {
    @Autowired
    private edcMapper edcMapper;
    @GetMapping("/getPredictResult")
    public List<PredictResult> getPredictResult() {
        return edcMapper.getPredictResult();
    }
    @GetMapping("/PredictDX")
    public Map<String, String> processStringDX(@RequestParam String input, @RequestParam String ifAD) {
        // 调用 .exe 文件
        String result = callExeFileDX(input,ifAD);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        response.put("ifAD", ifAD);
        return response;
    }
    @GetMapping("/PredictDL")
    public Map<String, String> processStringDL(@RequestParam String input, @RequestParam String ifAD) {
        // 调用 .exe 文件

        String result = callExeFileDL(input,ifAD);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return response;
    }
    private String callExeFileDX(String input,String ifAD) {
        try {
            String exePath = "scripts/edc_backend_models_v3/edc_backend_models_v3/qualitative_model/model.exe";
            // 设置工作目录
            File workingDirectory = new File("scripts/edc_backend_models_v3/edc_backend_models_v3/qualitative_model/");

            // 查询数据库以查找是否存在符合输入的记录
            PredictResult existingRecord = edcMapper.findBySmile(input);

            if (existingRecord != null) {
                String jsonFilePath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/DX";
                File resultFolder = new File(jsonFilePath); // 创建 File 对象以检查文件夹
                if (resultFolder.exists() && resultFolder.isDirectory()) {
                    String ADPath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/DX/AD";
                    File ADFolder = new File(ADPath); // 创建 File 对象以检查文件夹
                    String runJsonPath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/DX/runs.json"; // 构建 run.json 的路径
                    File runJsonFile = new File(runJsonPath); // 创建 File 对象以检查 run.json 文件
                    String[] files = ADFolder.list();
                    //数据库有记录且需要AD图 但AD文件夹里内容为空
                    if (files != null && "AD".equals(ifAD) && files.length == 0) {
                        String outFilePath = "../result/"+existingRecord.getId()+"/DX";
                        List<String> command = new ArrayList<>();
                        command.add(exePath);
                        command.add("-i");
                        command.add(input);
                        command.add("-o");
                        command.add(outFilePath);
                        command.add("-s");
                        // 调用 exe 文件
                        ProcessBuilder pb = new ProcessBuilder(command);
                        pb.directory(workingDirectory); // 设置工作目录
                        pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                        pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                        Process process = pb.start();
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            return "Error: Process exited with code " + exitCode;
                        }
                        // 读取生成的 JSON 文件
                        return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
                    }
                    if (!runJsonFile.exists() || !runJsonFile.isFile()) {
                        // run.json 文件不存在：
                        String outFilePath = "../result/"+existingRecord.getId()+"/DX";
                        List<String> command = new ArrayList<>();
                        command.add(exePath);
                        command.add("-i");
                        command.add(input);
                        command.add("-o");
                        command.add(outFilePath);
                        // 根据 ifAD 的值添加 "-s" 参数
                        if ("AD".equals(ifAD)) {
                            command.add("-s");
                        }
                        // 调用 exe 文件
                        ProcessBuilder pb = new ProcessBuilder(command);
                        pb.directory(workingDirectory); // 设置工作目录
                        pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                        pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                        Process process = pb.start();
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            return "Error: Process exited with code " + exitCode;
                        }
                        // 读取生成的 JSON 文件
                        return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
                    }
                    return readJsonFromFolder(jsonFilePath); // 按照文件夹读取结果
                } else {
                    //数据库有记录 但是没有DX文件夹
                    String outFilePath = "../result/"+existingRecord.getId()+"/DX";
                    List<String> command = new ArrayList<>();
                    command.add(exePath);
                    command.add("-i");
                    command.add(input);
                    command.add("-o");
                    command.add(outFilePath);
                    // 根据 ifAD 的值添加 "-s" 参数
                    if ("AD".equals(ifAD)) {
                        command.add("-s");
                    }
                    // 调用 exe 文件
                    ProcessBuilder pb = new ProcessBuilder(command);
                    pb.directory(workingDirectory); // 设置工作目录
                    pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                    pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        return "Error: Process exited with code " + exitCode;
                    }
                    // 读取生成的 JSON 文件
                    return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
                }
            } else {
                // 如果记录不存在，新增记录
                PredictResult newRecord = new PredictResult();
                newRecord.setSource(input);
                edcMapper.insertPredictResult(newRecord);
                String outFilePath = "../result/"+newRecord.getId()+"/DX";
                String jsonFilePath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/"+newRecord.getId()+"/DX";
                List<String> command = new ArrayList<>();
                command.add(exePath);
                command.add("-i");
                command.add(input);
                command.add("-o");
                command.add(outFilePath);
                // 根据 ifAD 的值添加 "-s" 参数
                if ("AD".equals(ifAD)) {
                    command.add("-s");
                }
                // 调用 exe 文件
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.directory(workingDirectory); // 设置工作目录
                pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    return "Error: Process exited with code " + exitCode;
                }
                // 读取生成的 JSON 文件
                return readJsonFromFolder(jsonFilePath);//按照文件夹读结果

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
    }
    private String callExeFileDL(String input,String ifAD) {
        try {
            String exePath = "scripts/edc_backend_models_v3/edc_backend_models_v3/quantitative_model/model.exe";
            // 设置工作目录
            File workingDirectory = new File("scripts/edc_backend_models_v3/edc_backend_models_v3/quantitative_model/");

            // 查询数据库以查找是否存在符合输入的记录
            PredictResult existingRecord = edcMapper.findBySmile(input);

            if (existingRecord != null) {
                String jsonFilePath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/DL";
                File resultFolder = new File(jsonFilePath); // 创建 File 对象以检查文件夹
                if (resultFolder.exists() && resultFolder.isDirectory()) {
                    String ADPath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/DL/AD";
                    File ADFolder = new File(ADPath); // 创建 File 对象以检查文件夹
                    String runJsonPath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/DL/runs.json"; // 构建 run.json 的路径
                    File runJsonFile = new File(runJsonPath); // 创建 File 对象以检查 run.json 文件
                    String[] files = ADFolder.list();
                    if (files != null && "AD".equals(ifAD) && files.length == 0) {
                        String outFilePath = "../result/"+existingRecord.getId()+"/DL";
                        List<String> command = new ArrayList<>();
                        command.add(exePath);
                        command.add("-i");
                        command.add(input);
                        command.add("-o");
                        command.add(outFilePath);
                        command.add("-s");
                        // 调用 exe 文件
                        ProcessBuilder pb = new ProcessBuilder(command);
                        pb.directory(workingDirectory); // 设置工作目录
                        pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                        pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                        Process process = pb.start();
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            return "Error: Process exited with code " + exitCode;
                        }
                        // 读取生成的 JSON 文件
                        return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
                    }
                    if (!runJsonFile.exists() || !runJsonFile.isFile()) {
                        // run.json 文件不存在：
                        String outFilePath = "../result/"+existingRecord.getId()+"/DL";
                        List<String> command = new ArrayList<>();
                        command.add(exePath);
                        command.add("-i");
                        command.add(input);
                        command.add("-o");
                        command.add(outFilePath);
                        // 根据 ifAD 的值添加 "-s" 参数
                        if ("AD".equals(ifAD)) {
                            command.add("-s");
                        }
                        // 调用 exe 文件
                        ProcessBuilder pb = new ProcessBuilder(command);
                        pb.directory(workingDirectory); // 设置工作目录
                        pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                        pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                        Process process = pb.start();
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            return "Error: Process exited with code " + exitCode;
                        }
                        // 读取生成的 JSON 文件
                        return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
                    }
                    return readJsonFromFolder(jsonFilePath); // 按照文件夹读取结果
                } else {
                    //数据库有记录 但是没有DX文件夹
                    String outFilePath = "../result/"+existingRecord.getId()+"/DL";
                    List<String> command = new ArrayList<>();
                    command.add(exePath);
                    command.add("-i");
                    command.add(input);
                    command.add("-o");
                    command.add(outFilePath);
                    // 根据 ifAD 的值添加 "-s" 参数
                    if ("AD".equals(ifAD)) {
                        command.add("-s");
                    }
                    // 调用 exe 文件
                    ProcessBuilder pb = new ProcessBuilder(command);
                    pb.directory(workingDirectory); // 设置工作目录
                    pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                    pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        return "Error: Process exited with code " + exitCode;
                    }
                    // 读取生成的 JSON 文件
                    return readJsonFromFolder(jsonFilePath);//按照文件夹读结果
                }
            } else {
                // 如果记录不存在，新增记录
                PredictResult newRecord = new PredictResult();
                newRecord.setSource(input);
                edcMapper.insertPredictResult(newRecord);
                String outFilePath = "../result/"+newRecord.getId()+"/DL";
                String jsonFilePath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/"+newRecord.getId()+"/DL";
                List<String> command = new ArrayList<>();
                command.add(exePath);
                command.add("-i");
                command.add(input);
                command.add("-o");
                command.add(outFilePath);
                // 根据 ifAD 的值添加 "-s" 参数
                if ("AD".equals(ifAD)) {
                    command.add("-s");
                }
                // 调用 exe 文件
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.directory(workingDirectory); // 设置工作目录
                pb.redirectOutput(new File("output.log")); // 将标准输出重定向到文件
                pb.redirectError(new File("error.log")); // 将错误输出重定向到文件
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    return "Error: Process exited with code " + exitCode;
                }
                // 读取生成的 JSON 文件
                return readJsonFromFolder(jsonFilePath);//按照文件夹读结果

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string: " + e.getMessage();
        }
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
        PredictResult existingRecord = edcMapper.findBySmile(smiles);
        if (existingRecord != null) {
            // 如果记录存在，开始下载
            String folderPath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/"+existingRecord.getId()+"/"+PreType;
            File file = new File(folderPath, fileName);
            if (file.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getEventImage")
    public ResponseEntity<ByteArrayResource> getEventImage(@RequestParam String PreType,String smiles,String id) throws IOException {
        PredictResult existingRecord = edcMapper.findBySmile(smiles);
        if (existingRecord != null) {
            // 记录存在，读取图片
            String folderPath = "scripts/edc_backend_models_v3/edc_backend_models_v3/result/" + existingRecord.getId() + "/" + PreType + "/AD/" + id + ".png";
            File file = new File(folderPath); // 文件路径
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
