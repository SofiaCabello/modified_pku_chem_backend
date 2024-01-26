package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户登录，返回token
     * @param user 用户名和密码
     * @return tokenMap
     */
    @PostMapping("/userLogin")
    public Result login(@RequestBody User user){
        String checkPassword = userMapper.getPassword(user.getUsername());
        if(checkPassword == null || !checkPassword.equals(user.getPassword())){
            return Result.fail().message("用户名或密码错误");
        } else {
            String username = user.getUsername();
            String token = JwtUtil.generateToken(username);
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            return Result.ok(tokenMap);
        }
    }

    /**
     * 获取用户信息
     * @param token 由前端传入的token
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public Result userInfo(String token){
        String username = JwtUtil.getUsername(token);
        String role = userMapper.getRole(username);
        String realName = userMapper.getRealName(username);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("username", username);
        resultMap.put("role", role);
        resultMap.put("realName", realName);
        return Result.ok(resultMap);
    }

    /**
     * 用户登出
     * @return 登出成功
     */
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok().message("登出成功");
    }
}
