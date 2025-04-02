package com.example.edcspring.controller;

import com.example.edcspring.entity.UserInfo;
import com.example.edcspring.service.AuthService;
//import com.example.edcspring.service.Impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private AuthService authService;  // 注入接口而不是实现类

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

    @PostMapping("/getUserInfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestBody Map<String, String> request) {
        String username = request.get("username"); // 从请求中获取用户名

        // 调用服务获取用户信息 - 使用新方法getUserInfoMap
        Map<String, Object> response = authService.getUserInfoMap(username);

        // 根据获取结果返回相应的响应
        if (response.get("success").equals(true)) {
            return ResponseEntity.ok(response); // 获取成功
        } else {
            return ResponseEntity.badRequest().body(response); // 获取失败
        }
    }

    //基于token的用户信息获取方法
    @GetMapping("/getUserInfoByToken")
    public ResponseEntity<Map<String, Object>> getUserInfoByToken(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 移除"Bearer "前缀
        }

        // 调用服务通过token获取用户信息
        Map<String, Object> response = authService.getUserInfoByToken(token);

        // 根据获取结果返回相应的响应
        if (response.get("success").equals(true)) {
            return ResponseEntity.ok(response); // 获取成功
        } else {
            return ResponseEntity.badRequest().body(response); // 获取失败
        }
    }
}
