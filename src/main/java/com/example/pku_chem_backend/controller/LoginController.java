package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import com.mysql.jdbc.log.Log;
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
    private LogUtil logUtil = new LogUtil();
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户登录，返回token
     * @param user 用户名和密码
     * @return tokenMap
     */
    @PostMapping("/userLogin")
    public Result login(
            @RequestBody User user,
            HttpServletRequest request
    ){
        String checkPassword = userMapper.getPassword(user.getUsername());
        if(checkPassword == null || !checkPassword.equals(user.getPassword())){
            return Result.fail().message("用户名或密码错误");
        } else {
            String username = user.getUsername();
            String token = JwtUtil.generateToken(username);
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            logUtil.writeLog(username, "LOGIN", "用户登录", java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
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
        List<String> usernames = userMapper.getAllUsernames();
        logUtil.createLogFileForCurrent(usernames);
        String username = JwtUtil.getUsername(token);
        Integer id = userMapper.getId(username);
        String role = userMapper.getRole(username);
        String realName = userMapper.getRealName(username);
        String[] roles = {role};
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        resultMap.put("username", username);
        resultMap.put("roles", roles);
        resultMap.put("realName", realName);
        return Result.ok(resultMap);
    }

    /**
     * 用户登出
     * @return 登出成功
     */
    @PostMapping("/logout")
    public Result logout(
            HttpServletRequest request
    ){
        logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "LOGOUT", "用户登出", java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("登出成功");
    }

}
