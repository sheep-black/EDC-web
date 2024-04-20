package com.example.edcspring.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@CrossOrigin
public class ParamsController {
    //http://localhost:8080/teststring?name=xyz
    @RequestMapping(value = "/teststring",method = RequestMethod.GET)
    public String testString(String name){
        System.out.println(name);
        //直接返回一个字符串
        return "大家好" + name;
    }

}
