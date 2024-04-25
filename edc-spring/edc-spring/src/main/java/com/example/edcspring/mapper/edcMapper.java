package com.example.edcspring.mapper;

import com.example.edcspring.entity.TestData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface edcMapper {
    @Select("SELECT * FROM testdata")
    public List<TestData> getAllData();
    @Select("SELECT * FROM testdata WHERE cas LIKE CONCAT('%', #{keyword}, '%')")
    List<TestData> searchData(@Param("keyword") String keyword);

}
