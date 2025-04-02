package com.example.edcspring.config;

import com.example.edcspring.interceptor.PredictLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private PredictLimitInterceptor predictLimitInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径配置跨域规则
                .allowedOrigins("http://49.234.4.144:81","http://sheepblack.cn:81","http://localhost:5173","https://edc.sheepblack.cn") // 允许来自指定源的跨域请求
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("*"); // 允许的请求头
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加预测次数限制拦截器，并指定拦截的路径
        registry.addInterceptor(predictLimitInterceptor)
                .addPathPatterns("/PredictDX","/PredictDL"); // 拦截所有预测相关的请求路径
        // 如果有需要排除的路径，可以使用 .excludePathPatterns("/api/predict/exclude/**");
    }
}
