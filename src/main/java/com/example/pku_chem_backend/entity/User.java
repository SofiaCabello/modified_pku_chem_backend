package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private Integer id; // 用户id
    private String username; // 用户名
    private String password; // 密码
    private String realName; // 真实姓名
    private String role; // 角色，分为管理员和普通用户
}
