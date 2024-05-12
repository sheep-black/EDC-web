package com.example.edcspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private edcMapper edcMapper;
    @GetMapping("/searchDATA")
    public List<TestData> getData(@RequestParam String columnName, @RequestParam String keyword) {
        return edcMapper.searchData(columnName,keyword);
    }

}
