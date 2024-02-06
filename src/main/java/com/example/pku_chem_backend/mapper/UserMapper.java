package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT id FROM User WHERE username = #{username}")
    Integer getId(String username);
    @Select("SELECT password FROM User WHERE username = #{username}")
    String getPassword(String username);
    @Select("SELECT role FROM User WHERE username = #{username}")
    String getRole(String username);
    @Select("SELECT real_name FROM User WHERE username = #{username}")
    String getRealName(String username);
    @Insert("INSERT INTO User (username, password, real_name, role) VALUES (#{username}, #{password}, #{realName}, #{role})")
    void insertUser(String username, String password, String realName, String role);
}
