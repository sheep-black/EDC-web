package com.example.edcspring.service;

import com.example.edcspring.entity.UserInfo;
import com.example.edcspring.mapper.UserInfoMapper;
import com.example.edcspring.util.Ip2RegionUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final Ip2RegionUtil ip2RegionUtil;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    public AuthService(UserInfoMapper userInfoMapper, PasswordEncoder passwordEncoder, Ip2RegionUtil ip2RegionUtil) {
        this.userInfoMapper = userInfoMapper;
        this.passwordEncoder = passwordEncoder;
        this.ip2RegionUtil = ip2RegionUtil;
    }

    /**
     * 生成JWT令牌
     *
     * @param userInfo 用户信息对象
     * @return JWT令牌字符串
     */
    public String generateToken(UserInfo userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userInfo.getUserName());
        claims.put("email", userInfo.getEmail());
        claims.put("vip", userInfo.getVip());
        claims.put("userId", userInfo.getUserId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userInfo.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
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

            // 2. 检查用户名是否已存在
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

            // 4. 登录成功，生成JWT令牌
            String token = generateToken(userInfo);

            // 5. 创建用户对象，去除敏感信息
            Map<String, Object> user = new HashMap<>();
            user.put("userName", userInfo.getUserName());
            user.put("email", userInfo.getEmail());
            user.put("vip", userInfo.getVip());
            user.put("predicttimes", userInfo.getPredicttimes());
            user.put("region", userInfo.getRegion());

            // 6. 构建返回结果
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("token", token);  // 添加token
            result.put("user", user);    // 添加用户对象

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

    /**
     * 更新用户预测次数
     *
     * @param userId 用户ID
     * @param predicttimes 预测次数
     * @return 更新结果
     */
    public boolean updatePredictTimes(int userId, Integer predicttimes) {
        try {
            userInfoMapper.updatePredictTimes(userId, predicttimes);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 减少用户预测次数
     *
     * @param userId 用户ID
     * @return 剩余预测次数，如果更新失败返回-1
     */
    public int decreasePredictTimes(int userId) {
        try {
            // 先获取当前用户信息
            UserInfo userInfo = userInfoMapper.findByUserId(userId);
            if (userInfo == null || userInfo.getPredicttimes() <= 0) {
                return 0;
            }

            // 减少预测次数
            int newPredicttimes = userInfo.getPredicttimes() - 1;
            userInfoMapper.updatePredictTimes(userId, newPredicttimes);
            return newPredicttimes;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 通过用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return UserInfo对象
     */
    public UserInfo getUserInfo(int userId) {
        return userInfoMapper.findByUserId(userId);
    }

    /**
     * 通过用户名获取格式化的用户信息
     *
     * @param username 用户名
     * @return 包含用户信息的Map
     */
    public Map<String, Object> getUserInfoMap(String username) {
        Map<String, Object> result = new HashMap<>();

        try {
            UserInfo userInfo = userInfoMapper.findByUsername(username);

            if (userInfo == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }

            // 创建用户对象，去除敏感信息
            Map<String, Object> user = new HashMap<>();
            user.put("userName", userInfo.getUserName());
            user.put("email", userInfo.getEmail());
            user.put("vip", userInfo.getVip());
            user.put("predicttimes", userInfo.getPredicttimes());
            user.put("region", userInfo.getRegion());

            result.put("success", true);
            result.put("message", "成功获取用户信息");
            result.put("user", user);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息失败: " + e.getMessage());
        }

        return result;
    }
    /**
     * 通过token获取用户信息
     *
     * @param token JWT令牌
     * @return 包含用户信息的响应Map
     */
    public Map<String, Object> getUserInfoByToken(String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证token是否有效
            if (token == null || token.isEmpty()) {
                response.put("success", false);
                response.put("message", "Token is missing");
                return response;
            }

            // 解析token获取用户名
            String username = getUsernameFromToken(token);
            if (username == null) {
                response.put("success", false);
                response.put("message", "Invalid token");
                return response;
            }

            // 从数据库查询用户信息
            UserInfo userInfo = userInfoMapper.findByUsername(username);
            if (userInfo == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }

            // 构建用户信息对象
            Map<String, Object> user = new HashMap<>();
            user.put("userName", userInfo.getUserName());
            user.put("email", userInfo.getEmail());
            user.put("vip", userInfo.getVip());
            user.put("predicttimes", userInfo.getPredicttimes());
            user.put("region", userInfo.getRegion());

            // 封装响应
            response.put("success", true);
            response.put("message", "成功获取用户信息");
            response.put("user", user);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to process token: " + e.getMessage());
        }

        return response;
    }

    /**
     * 从JWT令牌中提取用户名
     *
     * @param token JWT令牌
     * @return 用户名，如果解析失败则返回null
     */
    private String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null; // 解析失败返回null
        }
    }

}

