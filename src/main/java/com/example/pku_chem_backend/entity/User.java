package com.example.pku_chem_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import nonapi.io.github.classgraph.json.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@ToString
@Builder
public class User {
    // 声明id是自动生成的
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 用户id
    private String username; // 用户名
    private String password; // 密码
    private String realName; // 真实姓名
    private String role; // 角色，分为管理员和普通用户
    private String email;
}
