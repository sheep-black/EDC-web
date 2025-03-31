package com.example.edcspring.controller;

import com.example.edcspring.entity.AOPData;
import com.example.edcspring.entity.EventData;
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
    @GetMapping("/searchEvent")
    public List<EventData> searchEvent(@RequestParam String columnName, @RequestParam String keyword) {
        return edcMapper.searchEvent(columnName,keyword);
    }
    @GetMapping("/searchEventAOP")
    public EventData searchEventAOP(@RequestParam String eventID) {
        return edcMapper.searchEventAOP(eventID);
    }

    @GetMapping("/findDistinct")
    public List<String> findDistinctField(@RequestParam String fieldName) {
        return edcMapper.findDistinctFieldNames(fieldName);
    }
    @GetMapping("/findID")
    public List<TestData> findID(@RequestParam String ID) {
        int id = Integer.parseInt(ID); // 将字符串类型的 ID 转换为整数
        List<TestData> result = edcMapper.findByID(id);
        return edcMapper.findByID(id);
    }
    @GetMapping("/findAOPID")
    public List<AOPData> findAOPID(@RequestParam String ID) {
        int id = Integer.parseInt(ID); // 将字符串类型的 ID 转换为整数
        List<AOPData> result = edcMapper.findByAOPID(id);
        return edcMapper.findByAOPID(id);
    }
    @PostMapping("/dataScreen")
    public List<TestData> Screen(@RequestBody ScreenData filterRequest) {
        // 获取前端发送的筛选条件
        List<String> assay = filterRequest.getAssay();
        List<String> endpoints = filterRequest.getEndpoint();
        String activities = filterRequest.getActivity();
        // 调用TestDataMapper中的方法进行数据库查询
        return edcMapper.combinedFilter(endpoints,assay, activities);
    }

}
