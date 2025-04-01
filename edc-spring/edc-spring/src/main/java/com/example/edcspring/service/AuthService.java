package com.example.edcspring.service;

import com.example.edcspring.entity.UserInfo;
import com.example.edcspring.mapper.UserInfoMapper;
import com.example.edcspring.util.Ip2RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final Ip2RegionUtil ip2RegionUtil;

    @Autowired
    public AuthService(UserInfoMapper userInfoMapper, PasswordEncoder passwordEncoder, Ip2RegionUtil ip2RegionUtil) {
        this.userInfoMapper = userInfoMapper;
        this.passwordEncoder = passwordEncoder;
        this.ip2RegionUtil = ip2RegionUtil;
    }

    /**
     * 用户注册服务
     *
     * @param userInfo 用户信息对象
     * @param ipAddress 客户端IP地址
     * @return 包含注册结果的Map
     */
    public Map<String, Object> register(UserInfo userInfo, String ipAddress) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 输入验证
            if (userInfo.getUserName() == null || userInfo.getUserName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Username cannot be empty");
                return result;
            }

            if (userInfo.getPassword() == null || userInfo.getPassword().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Password cannot be empty");
                return result;
            }

            if (userInfo.getEmail() == null || userInfo.getEmail().trim().isEmpty() || !isValidEmail(userInfo.getEmail())) {
                result.put("success", false);
                result.put("message", "Email format is incorrect");
                return result;
            }

//             2. 检查用户名是否已存在
//            System.out.println(userInfo.getUserName());
            if (userInfoMapper.findByUsername(userInfo.getUserName()) != null) {
                result.put("success", false);
                result.put("message", "Username already exists");
                return result;
            }

            // 3. 检查邮箱是否已存在
            if (userInfoMapper.findByEmail(userInfo.getEmail()) != null) {
                result.put("success", false);
                result.put("message", "Email already exists");
                return result;
            }

            // 4. 密码加密
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

            // 5. 获取IP地区信息
            String region = ip2RegionUtil.getRegionFromIp(ipAddress);

            // 6. 设置用户信息
            userInfo.setRegion(region);
            userInfo.setVip(0);
            userInfo.setPredicttimes(10);

            // 7. 插入用户信息到数据库
            userInfoMapper.insertUser(userInfo);

            // 8. 设置成功响应
            result.put("success", true);
            result.put("message", "注册成功");
            result.put("username", userInfo.getUserName());
            result.put("region", region);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 验证邮箱格式是否有效
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * 用户登录服务
     *
     * @param username 用户名
     * @param password 密码
     * @return 包含登录结果的Map
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 查询用户
            UserInfo userInfo = userInfoMapper.findByUsername(username);

            // 2. 验证用户是否存在
            if (userInfo == null) {
                result.put("success", false);
                result.put("message", "用户名不存在");
                return result;
            }

            // 3. 验证密码
            if (!passwordEncoder.matches(password, userInfo.getPassword())) {
                result.put("success", false);
                result.put("message", "密码不正确");
                return result;
            }

            // 4. 登录成功，返回用户信息（不包含敏感数据）
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("username", userInfo.getUserName());
            result.put("email", userInfo.getEmail());
            result.put("vip", userInfo.getVip());
            result.put("predicttimes", userInfo.getPredicttimes());
            result.put("region", userInfo.getRegion());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 更新用户密码
     *
     * @param username 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 包含更新结果的Map
     */
    public Map<String, Object> updatePassword(String username, String oldPassword, String newPassword) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 查询用户
            UserInfo userInfo = userInfoMapper.findByUsername(username);

            // 2. 验证用户是否存在
            if (userInfo == null) {
                result.put("success", false);
                result.put("message", "用户名不存在");
                return result;
            }

            // 3. 验证旧密码
            if (!passwordEncoder.matches(oldPassword, userInfo.getPassword())) {
                result.put("success", false);
                result.put("message", "原密码不正确");
                return result;
            }

            // 4. 加密新密码
            String encodedNewPassword = passwordEncoder.encode(newPassword);

            // 5. 更新密码 - 使用用户ID
            userInfoMapper.updatePassword(userInfo.getUserId(), encodedNewPassword);

            // 6. 设置成功响应
            result.put("success", true);
            result.put("message", "密码更新成功");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "密码更新失败: " + e.getMessage());
        }

        return result;
    }
}
