package com.example.edcspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestFind {
    @Autowired
    private edcMapper edcMapper;
    @GetMapping("/getData")
    public List<TestData> getData() {
        return edcMapper.getAllData(); // 直接调用Mapper接口的方法
    }
//    public String query(){
//        List<testdata> list=edcMapper.getAllData();
//        System.out.print(list);
//        return "yes";
//    }
}