package com.example.edcspring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    //访问地址--teststring是等下访问url时要加的地址
    @RequestMapping("/teststring")
    //直接返回一个字符串
    @ResponseBody
    public String testString(){
        return "大家好";
    }

}
