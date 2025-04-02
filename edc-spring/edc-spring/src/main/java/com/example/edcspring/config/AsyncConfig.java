package com.example.edcspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @PostConstruct
    public void init() {
        // 在应用启动时设置安全上下文传递策略
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("PredictThread-");
        executor.initialize();

        // 使用安全上下文委托包装器确保安全上下文传递
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}

