package com.example.edcspring.service;

import com.example.edcspring.entity.UserInfo;
import java.util.Map;

public interface AuthService {
    /**
     * 生成JWT令牌
     */
    String generateToken(UserInfo userInfo);

    /**
     * 用户注册服务
     */
    Map<String, Object> register(UserInfo userInfo, String ipAddress);

    /**
     * 用户登录服务
     */
    Map<String, Object> login(String username, String password);

    /**
     * 更新用户密码
     */
    Map<String, Object> updatePassword(String username, String oldPassword, String newPassword);

    /**
     * 更新用户预测次数
     */
    boolean updatePredictTimes(int userId, Integer predicttimes);

    /**
     * 减少用户预测次数
     */
    int decreasePredictTimes(int userId);

    /**
     * 通过用户ID获取用户信息
     */
    UserInfo getUserInfo(int userId);

    /**
     * 通过用户名获取格式化的用户信息
     */
    Map<String, Object> getUserInfoMap(String username);

    /**
     * 通过token获取用户信息
     */
    Map<String, Object> getUserInfoByToken(String token);
}
