package com.example.edcspring.controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestParams {

    @GetMapping("/sendData")
    public String receiveData(@RequestParam String data) {
        System.out.println("Received data from Vue: " + data);
        return "Data received by Spring Boot";
    }
}
