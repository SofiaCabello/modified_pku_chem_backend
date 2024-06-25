package com.example.pku_chem_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.service.UserService;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import com.mysql.jdbc.log.Log;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

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
        List<User> result = userService.getUser(page, limit, username, realName, id, role, sort);
        return Result.ok(result).total(result.size()).message("获取用户列表成功");
    }

    @PostMapping("/createUser")
    public Result createUser(@RequestBody User user, HttpServletRequest request){
        if(userService.createUser(user)){
            return Result.ok().message("用户创建成功");
        }
        return Result.fail().message("用户创建失败");
    }

    @GetMapping("/getRole")
    public Result getRole(@RequestHeader("Authorization") String token){
        String username = JwtUtil.getUsername(token);
        return Result.ok(userService.getRole(username)).message("获取用户角色成功");
    }

    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestParam Integer id){
        if(userService.deleteUser(id)){
            return Result.ok().message("用户删除成功");
        }
        return Result.fail().message("用户删除失败");
    }

    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user){
        if(userService.updateUser(user)){
            return Result.ok().message("用户更新成功");
        }
        return Result.fail().message("用户更新失败");
    }
}
