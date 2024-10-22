package com.example.edcspring.controller;

import com.example.edcspring.entity.ResultDL;
import com.example.edcspring.entity.ResultDX;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.edcspring.mapper.edcMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class SearchDXDLController {
    @Autowired
    private edcMapper edcMapper;
    @GetMapping("/searchSMILESDX")
    public Map<String, Object> searchSMILESDX(
            @RequestParam String smiles,
            @RequestParam int pagenum,
            @RequestParam int pagesize) {
        int offset = pagenum * pagesize;
        List<ResultDX> data = edcMapper.searchSMILESDX(smiles, offset, pagesize);
        int total = edcMapper.countSMILESDX(smiles);
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("total", total);
        return response;
    }
    @GetMapping("/getDXResult")
    public List<ResultDX> getDXResult(
            @RequestParam int pagenum,
            @RequestParam int pagesize) {
        int offset = pagenum * pagesize;
        return edcMapper.getDXResult(offset, pagesize);
    }
    @GetMapping("/searchSMILESDL")
    public Map<String, Object> searchSMILESDL(
            @RequestParam String smiles,
            @RequestParam int pagenum,
            @RequestParam int pagesize) {
        int offset = pagenum * pagesize;
        List<ResultDL> data = edcMapper.searchSMILESDL(smiles, offset, pagesize);
        int total = edcMapper.countSMILESDL(smiles);
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("total", total);
        return response;
    }
    @GetMapping("/getDLResult")
    public List<ResultDL> getDLResult(
            @RequestParam int pagenum,
            @RequestParam int pagesize) {
        int offset = pagenum * pagesize;
        return edcMapper.getDLResult(offset, pagesize);
    }
}
