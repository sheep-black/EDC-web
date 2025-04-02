package com.example.edcspring.interceptor;

import com.example.edcspring.entity.UserInfo;
import com.example.edcspring.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PredictLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求中获取用户ID (JWT过滤器已经解析并设置)
        Object userIdObj = request.getAttribute("userId");
        if (userIdObj == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "未授权访问");

            PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(result));
            out.flush();
            return false;
        }

        // 将userId转换为int类型
        int userId = Integer.parseInt(userIdObj.toString());

        // 获取并检查用户预测次数
        UserInfo userInfo = authService.getUserInfo(userId);
        if (userInfo == null || userInfo.getPredicttimes() <= 0) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "没有剩余预测次数");
            result.put("predictTimesRemaining", 0);

            PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(result));
            out.flush();
            return false;
        }

        // 减少预测次数
        int remainingTimes = authService.decreasePredictTimes(userId);
        if (remainingTimes < 0) {
            response.setStatus(500);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "更新预测次数失败");

            PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(result));
            out.flush();
            return false;
        }

        // 将剩余次数添加到request属性中，以便控制器使用
        request.setAttribute("predictTimesRemaining", remainingTimes);

        // 允许继续处理请求
        return true;
    }
}
