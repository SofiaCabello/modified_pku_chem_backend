package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public String getRealName(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper).getRealName();
    }

    public List<User> getUser(Integer page, Integer limit, String username, String realName, Integer id, String role, String sort){
        Page<User> pageParam = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(username != null){
            wrapper.like("username", username);
        }
        if(realName != null){
            wrapper.like("real_name", realName);
        }
        if(role != null){
            wrapper.eq("role", role);
        }
        if(id != null){
            wrapper.eq("id", id);
        }
        switch (sort){
            case "+id" -> wrapper.orderByAsc("id");
            case "-id" -> wrapper.orderByDesc("id");
        }
        userMapper.selectPage(pageParam, wrapper);
        return pageParam.getRecords();
    }

    public boolean createUser(User user){
        return userMapper.insert(user) == 1;
    }

    public String getRole(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper).getRole();
    }

    public boolean deleteUser(Integer id){
        return userMapper.deleteById(id) == 1;
    }

    public boolean updateUser(User user){
        return userMapper.updateById(user) == 1;
    }


}
