package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/userLogin")
    public Result login(@RequestBody User user){
        return Result.ok().message("登录成功");
    }
}
