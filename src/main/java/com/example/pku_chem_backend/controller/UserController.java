package com.example.pku_chem_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import com.mysql.jdbc.log.Log;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    private LogUtil logUtil = new LogUtil();

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getUser")
    public Result getUser(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="username", required = false) String username,
            @RequestParam(value="realName", required = false) String realName,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value="role", required = false) String role,
            @RequestParam(value="sort", defaultValue = "+id") String sort
    ){
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
        return Result.ok(pageParam.getRecords()).total(pageParam.getTotal());
    }

    @PostMapping("/createUser")
    public Result createUser(
            @RequestBody User user,
            HttpServletRequest request){
        System.out.println(user);
        userMapper.insertUser(user.getUsername(), user.getPassword(), user.getRealName(), user.getRole());
        LogUtil.createLogFile(user.getUsername());
        logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "CREATE", "创建用户："+user.getUsername(), java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("用户创建成功");
    }

    @GetMapping("/getRole")
    public Result getRole(
            @RequestHeader("Authorization") String token
    ){
        String username = JwtUtil.getUsername(token);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        return Result.ok(user.getRole());
    }

    @PostMapping("/deleteUser")
    public Result deleteUser(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer id,
            HttpServletRequest request
    ){
        if (getUserName(token)) return Result.fail().message("非法访问" );
        String username = userMapper.selectById(id).getUsername();
        userMapper.deleteById(id);
        logUtil.writeLog(JwtUtil.getUsername(token), "DELETE", "删除用户："+username, java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        logUtil.deleteLogFile(username);
        return Result.ok().message("用户删除成功");
    }

    @PostMapping("/updateUser")
    public Result updateUser(
            @RequestBody User user,
            @RequestHeader("Authorization") String token,
            HttpServletRequest request
    ){
        if (getUserName(token)) return Result.fail().message("非法访问");
        userMapper.updateById(user);
        logUtil.writeLog(JwtUtil.getUsername(token), "UPDATE", "更新用户："+user.getUsername(), java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("用户信息更新成功");
    }

    private boolean getUserName(@RequestHeader("Authorization") String token) {
        String username = JwtUtil.getUsername(token);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        if(!userMapper.selectOne(wrapper).getRole().equals("admin")){
            return true;
        }
        return false;
    }
}
