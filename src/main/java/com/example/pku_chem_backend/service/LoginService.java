package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pku_chem_backend.dto.UserInfoDTO;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.EmailUtil;
import com.example.pku_chem_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailUtil emailUtil;

    public Map<String, Object> login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User checkUser = userMapper.selectOne(queryWrapper);
        if (checkUser == null || !checkUser.getPassword().equals(user.getPassword())) {
            return null;
        } else {
            String token = JwtUtil.generateToken(user.getUsername());
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            return tokenMap;
        }
    }

    public UserInfoDTO getUserInfo(String token){
        String username = JwtUtil.getUsername(token);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return UserInfoDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(new String[]{user.getRole()})
                .realName(user.getRealName())
                .build();
    }

    /**
     * 登出
     */
    public void logout(){}

    public boolean sendVerificationCode(String email){
        return emailUtil.sendCode(email);
    }

    public boolean verifyCode(String email, String code){
        return emailUtil.isCorrect(email, code);
    }
}
