package com.example.edcspring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.edcspring.mapper")
public class EdcSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdcSpringApplication.class, args);
    }

}
