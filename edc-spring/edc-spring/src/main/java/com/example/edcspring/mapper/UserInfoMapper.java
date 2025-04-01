package com.example.edcspring.mapper;

import com.example.edcspring.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper {

    @Insert("INSERT INTO user_info (username, password, email, region, predicttimes, vip) " +
            "VALUES (#{userName}, #{password}, #{email}, #{region}, #{predicttimes}, #{vip})")
    void insertUser(UserInfo userInfo);

    @Select("SELECT * FROM user_info WHERE username = #{username}")
    UserInfo findByUsername(String username);

    @Select("SELECT * FROM user_info WHERE email = #{email}")
    UserInfo findByEmail(String email);

    // 按用户ID更新密码
    @Update("UPDATE user_info SET password = #{newPassword} WHERE id = #{userId}")
    int updatePassword(int userId, String newPassword);
}
