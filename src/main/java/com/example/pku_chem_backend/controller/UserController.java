package com.example.pku_chem_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getUser")
    public Result getUser(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="username", required = false) String username,
            @RequestParam(value="realName", required = false) String realName,
            @RequestParam(value="role", required = false) String role){
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
        userMapper.selectPage(pageParam, wrapper);
        return Result.ok(pageParam.getRecords()).total(pageParam.getTotal());
    }

    @PostMapping("/createUser")
    public Result createUser(@RequestBody User user){
        System.out.println(user);
        userMapper.insertUser(user.getUsername(), user.getPassword(), user.getRealName(), user.getRole());
        return Result.ok().message("用户创建成功");
    }
}
