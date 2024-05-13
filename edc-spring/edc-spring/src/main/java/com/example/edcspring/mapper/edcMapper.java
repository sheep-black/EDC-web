package com.example.edcspring.mapper;

import com.example.edcspring.entity.TestData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface edcMapper {
    @Select("SELECT * FROM refine")
    List<TestData> getAllData();
//    @Select("SELECT * FROM refine WHERE cas LIKE CONCAT('%', #{keyword}, '%')")
//    List<TestData> searchData(@Param("keyword") String keyword);

    @Select("SELECT * FROM refine WHERE ${columnName} LIKE CONCAT('%', #{keyword}, '%')")
    List<TestData> searchData(@Param("columnName") String columnName, @Param("keyword") String keyword);

    @Select("SELECT DISTINCT ${fieldName} FROM refine")
    List<String> findDistinctFieldNames(@Param("fieldName") String fieldName);

    @SelectProvider(type = TestSqlProvider.class, method = "combinedFilter")
    List<TestData> combinedFilter(
            @Param("endpoints") String endpoints,
            @Param("assays") String assays,
            @Param("activities") String activities
    );

}
