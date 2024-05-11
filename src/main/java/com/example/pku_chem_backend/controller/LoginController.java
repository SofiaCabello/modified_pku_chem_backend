package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.service.LoginService;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginService loginService;

    /**
     * 用户登录，返回token
     * @param user 用户名和密码
     * @return tokenMap
     */
    @PostMapping("/userLogin")
    public Result login(@RequestBody User user){
        Map<String, Object> tokenMap = loginService.login(user);
        if (tokenMap == null) {
            return Result.fail().message("用户名或密码错误");
        } else {
            return Result.ok(tokenMap).message("登录成功");
        }
    }

    /**
     * 获取用户信息
     * @param token 由前端传入的token
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public Result userInfo(String token){
        try {
            return Result.ok(loginService.getUserInfo(token)).message("获取用户信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail().message("获取用户信息失败");
        }
    }

    /**
     * 用户登出
     * @return 登出成功
     */
    @PostMapping("/logout")
    public Result logout(){
        loginService.logout();
        return Result.ok().message("登出成功");
    }
}
