package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT password FROM User WHERE username = #{username}")
    String getPassword(String username);
    @Select("SELECT role FROM User WHERE username = #{username}")
    String getRole(String username);
    @Select("SELECT real_name FROM User WHERE username = #{username}")
    String getRealName(String username);
}
