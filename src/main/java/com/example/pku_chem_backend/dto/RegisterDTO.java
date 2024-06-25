package com.example.pku_chem_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO implements Serializable {
    private String email;
    private String verificationCode;
    private String password;
    private String realName;
    private String username;
}
