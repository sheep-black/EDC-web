package com.example.edcspring.mapper;

import com.example.edcspring.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface edcMapper {
    @Select("SELECT * FROM refine")
    List<TestData> getAllData();
//    @Select("SELECT * FROM refine WHERE cas LIKE CONCAT('%', #{keyword}, '%')")
//    List<TestData> searchData(@Param("keyword") String keyword);

    @Select("SELECT * FROM result_dx WHERE SMILES LIKE CONCAT('%', #{smiles}, '%') LIMIT #{offset}, #{limit}")
    List<ResultDX> searchSMILESDX(@Param("smiles") String smiles, @Param("offset") int offset, @Param("limit") int limit);
    @Select("SELECT COUNT(*) FROM result_dx WHERE SMILES LIKE CONCAT('%', #{smiles}, '%')")
    int countSMILESDX(@Param("smiles") String smiles);
    @Select("SELECT * FROM result_dx LIMIT #{offset}, #{limit}")
    List<ResultDX> getDXResult(@Param("offset") int offset, @Param("limit") int limit);
    @Select("SELECT * FROM result_dl WHERE SMILES LIKE CONCAT('%', #{smiles}, '%') LIMIT #{offset}, #{limit}")
    List<ResultDL> searchSMILESDL(@Param("smiles") String smiles, @Param("offset") int offset, @Param("limit") int limit);
    @Select("SELECT COUNT(*) FROM result_dl WHERE SMILES LIKE CONCAT('%', #{smiles}, '%')")
    int countSMILESDL(@Param("smiles") String smiles);
    @Select("SELECT * FROM result_dl LIMIT #{offset}, #{limit}")
    List<ResultDL> getDLResult(@Param("offset") int offset, @Param("limit") int limit);
    @Select("SELECT * FROM refine WHERE ${columnName} LIKE CONCAT('%', #{keyword}, '%')")
    List<TestData> searchData(@Param("columnName") String columnName, @Param("keyword") String keyword);
    @Select("SELECT * FROM event_title WHERE ${columnName} LIKE CONCAT('%', #{keyword}, '%')")
    List<EventData> searchEvent(@Param("columnName") String columnName, @Param("keyword") String keyword);


    @Select("SELECT * FROM event_title")
    List<EventData> getEventData();

    @Select("SELECT DISTINCT ${fieldName} FROM refine")
    List<String> findDistinctFieldNames(@Param("fieldName") String fieldName);

    @Select("SELECT * FROM refine WHERE id = #{id}")
    List<TestData> findByID(@Param("id") int id);
    @Select("SELECT * FROM aop WHERE AOP_ID = #{id}")
    List<AOPData> findByAOPID(@Param("id") int id);

    @Select("SELECT * FROM event_title WHERE EventID = #{eventID}")
    EventData searchEventAOP(@Param("eventID") String eventID);

    @SelectProvider(type = TestSqlProvider.class, method = "combinedFilter")
    List<TestData> combinedFilter(@Param("endpoint") List<String> endpoint,
                                  @Param("assay") List<String> assay,
                                  @Param("activity") String activity);
    @Select("SELECT * FROM predict_result")
    List<PredictResult> getPredictResult();

    @Select("SELECT * FROM predict_result WHERE smile = #{smile}")
    PredictResult findBySmile(String smile);

    @Insert("INSERT INTO predict_result (smile) VALUES (#{smile})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 获取自增 ID
    void insertPredictResult(PredictResult predictResult);


}
