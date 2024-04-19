package com.example.pku_chem_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetDrugDTO implements Serializable {
    private Integer page = 1; // 设定默认值
    private Integer limit = 10; // 设定默认值
    private String sort = "+id"; // 设定默认值
    private Integer id;
    private String name;
    private String producer;
    private String formula;
    private String cas;
    private String lab;
    private String location;
    private String layer;
    private String note;
    private String realName;
}
