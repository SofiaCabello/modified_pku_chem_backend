package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private String username; // 用户名，视作用户账号以及唯一标识
    private String password; // 密码
    private String role; // 角色，分为管理员和普通用户
}
