package com.example.edcspring.mapper;

import io.micrometer.common.util.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

public class TestSqlProvider {
    public String combinedFilter(Map<String, Object> parameters) {
        List<String> endpoint = (List<String>) parameters.get("endpoint");
        List<String> assay = (List<String>) parameters.get("assay");
        String activity = (String) parameters.get("activity");

        String sql = new SQL() {{
            SELECT("*");
            FROM("refine");

            boolean hasCondition = false;

            if (endpoint != null && endpoint.size() > 0) {
                String endpointIn = endpoint.stream().map(e -> "'" + e + "'").collect(Collectors.joining(", "));
                WHERE("endpoint IN (" + endpointIn + ")");
                hasCondition = true;
            }

            if (assay != null && assay.size() > 0) {
                String assayIn = assay.stream().map(a -> "'" + a + "'").collect(Collectors.joining(", "));
                if (hasCondition) {
                    WHERE("assay IN (" + assayIn + ")");
                } else {
                    WHERE("assay IN (" + assayIn + ")");
                    hasCondition = true;
                }
            }

            if (activity != null&& !activity.equals("")) {
                if (hasCondition) {
                    WHERE("activity = '" + activity + "'");
                } else {
                    WHERE("activity = '" + activity + "'");
                    hasCondition = true;
                }
            }
        }}.toString();

        System.out.println("Generated SQL query: " + sql); // 打印生成的 SQL 语句
        return sql;
    }
}
