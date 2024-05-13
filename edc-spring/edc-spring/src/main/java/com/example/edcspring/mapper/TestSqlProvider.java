package com.example.edcspring.mapper;

import io.micrometer.common.util.StringUtils;

import java.util.Map;

public class TestSqlProvider {
    public String combinedFilter(Map<String, Object> params) {
        String sql = "SELECT * FROM refine WHERE 1=1";
        System.out.println("1111111111111");
        if (StringUtils.isNotBlank((String) params.get("endpoints"))) {
            System.out.println("2");
            sql += " AND endpoint IN (#{endpoints})";
        }
        if (StringUtils.isNotBlank((String) params.get("assays"))) {
            System.out.println("2");
            sql += " AND assay IN (#{assays})";
        }
        if (StringUtils.isNotBlank((String) params.get("activities"))) {
            System.out.println("3");
            sql += " AND activity IN (#{activities})";
        }
        System.out.println("sql"+sql);
        return sql;
    }
}
