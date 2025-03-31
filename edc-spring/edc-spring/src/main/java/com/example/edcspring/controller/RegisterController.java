package com.example.edcspring.controller;

import com.example.edcspring.entity.UserInfo;
import com.example.edcspring.util.Ip2RegionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    private final Ip2RegionUtil ip2RegionUtil;

    @Autowired
    public RegisterController(Ip2RegionUtil ip2RegionUtil) {
        this.ip2RegionUtil = ip2RegionUtil;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo request, HttpServletRequest httpRequest) {
        String username = request.getUserName();
        String password = request.getPassword();
        String email = request.getEmail();
        String ipAddress = httpRequest.getRemoteAddr();

        // 查询IP地区
        String ipRegion = ip2RegionUtil.getRegionFromIp(ipAddress);

        System.out.println("IP地址: " + ipAddress);
        System.out.println("IP地区: " + ipRegion);

        // TODO: 这里可以添加注册逻辑，例如验证和保存用户信息

        return "Registration successful for user: " + username + ", IP location: " + ipRegion;
    }
}
