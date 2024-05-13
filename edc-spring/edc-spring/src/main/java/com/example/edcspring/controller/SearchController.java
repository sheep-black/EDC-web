package com.example.edcspring.controller;

import com.example.edcspring.entity.ScreenData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.entity.TestData;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {
    @Autowired
    private edcMapper edcMapper;
    @GetMapping("/searchDATA")
    public List<TestData> getData(@RequestParam String columnName, @RequestParam String keyword) {
        return edcMapper.searchData(columnName,keyword);
    }

    @GetMapping("/findDistinct")
    public List<String> findDistinctField(@RequestParam String fieldName) {
        return edcMapper.findDistinctFieldNames(fieldName);
    }

    @PostMapping("/dataScreen")
    public List<TestData> Screen(@RequestBody ScreenData filterRequest) {
        String[] endpoints = filterRequest.getEndpoint();
        String[] assays = filterRequest.getAssay();
        String[] activities = filterRequest.getActivity();
        String endpointString = Arrays.stream(endpoints).collect(Collectors.joining(","));
        String assayString = Arrays.stream(assays).collect(Collectors.joining(","));
        String activityString = Arrays.stream(activities).collect(Collectors.joining(","));
        if(endpointString.isEmpty()){
            System.out.println("111111111111");

        }
        List<TestData> filterRes = edcMapper.combinedFilter(endpointString,assayString,activityString);
        return filterRes;
    }

}
