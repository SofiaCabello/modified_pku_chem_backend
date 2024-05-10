package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public String getRealName(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper).getRealName();
    }
}
