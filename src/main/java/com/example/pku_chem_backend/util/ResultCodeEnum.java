package com.example.pku_chem_backend.util;

import lombok.Data;
import lombok.Getter;

import javax.xml.transform.Result;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败");

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
