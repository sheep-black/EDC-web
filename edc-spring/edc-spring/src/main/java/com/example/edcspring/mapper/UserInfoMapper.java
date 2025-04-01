package com.example.edcspring.mapper;

import com.example.edcspring.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {

    @Insert("INSERT INTO user_info (userName, password, email, region, predicttimes, vip) " +
            "VALUES (#{userName}, #{password}, #{email}, #{region}, #{predicttimes}, #{vip})")
    void insertUser(UserInfo userInfo);

    @Select("SELECT * FROM user_info WHERE userName = #{username}")
    UserInfo findByUsername(String username);

    @Select("SELECT * FROM user_info WHERE email = #{email}")
    UserInfo findByEmail(String email);

    // 按用户ID更新密码
    @Update("UPDATE user_info SET password = #{newPassword} WHERE userId = #{userId}")
    int updatePassword(int userId, String newPassword);

    // 根据用户ID查询用户信息
    @Select("SELECT * FROM user_info WHERE userId = #{userId}")
    UserInfo findByUserId(@Param("userId") int userId);

    // 更新用户预测次数
    @Update("UPDATE user_info SET predicttimes = #{predicttimes} WHERE userId = #{userId}")
    void updatePredictTimes(@Param("userId") int userId, @Param("predicttimes") Integer predicttimes);
}
