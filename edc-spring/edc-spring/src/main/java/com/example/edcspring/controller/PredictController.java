package com.example.edcspring.controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class PredictController {

    @GetMapping("/Predict")
    public Map<String, String> processString(@RequestParam String input) {
        // 调用 Python 脚本
        String result = callPythonScript(input);

        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return response;
    }

    private String callPythonScript(String input) {
        try {
            // 确保脚本路径正确
            String scriptPath = "src/scripts/script.py";
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath, input);
            System.out.println(scriptPath);
            Process process = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = in.readLine();
            in.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing string";
        }
    }

}
