package com.example.edcspring.controller;

import com.example.edcspring.entity.UserInfo;
import com.example.edcspring.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegisterController {

    private final AuthService authService;

    @Autowired
    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserInfo request, HttpServletRequest httpRequest) {
//        String ipAddress = httpRequest.getRemoteAddr(); // 获取真实IP地址
        String ipAddress = "49.234.4.144"; // 获取真实IP地址
        Map<String, Object> response = authService.register(request, ipAddress);
        if (response.get("success").equals(true)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserInfo request) {
        String username = request.getUserName(); // 从请求中获取用户名
        String password = request.getPassword(); // 从请求中获取密码
        // 调用服务进行登录验证
        Map<String, Object> response = authService.login(username, password);
        // 根据登录结果返回相应的响应
        if (response.get("success").equals(true)) {
            return ResponseEntity.ok(response); // 登录成功
        } else {
            return ResponseEntity.badRequest().body(response); // 登录失败
        }
    }

}
