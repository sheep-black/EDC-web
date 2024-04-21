package com.example.edcspring.mapper;

import com.example.edcspring.entity.testdata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface edcMapper {
    @Select("SELECT * FROM testdata")
    public List<testdata> getAllData();
}
