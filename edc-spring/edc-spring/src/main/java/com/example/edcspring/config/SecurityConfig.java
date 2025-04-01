package com.example.edcspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 禁用CSRF保护
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/register", "/login").permitAll()  // 允许注册和登录端点无需认证
//                        .anyRequest().authenticated()  // 其他所有请求需要认证
                        .anyRequest().permitAll()  // 临时允许所有请求，用于测试
                )
                .httpBasic(withDefaults());  // 使用新的方法语法

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
