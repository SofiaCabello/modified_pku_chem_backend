package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class Drug {
    private String drugName; // 试剂名称
    private String drugNickname; // 试剂别名
    private String drugFormula; // 试剂化学式
    private String drugCAS; // 试剂CAS号
    private String drugProducer; // 试剂生产商
    private String drugSpecification; // 试剂规格
    private String drugLocation; // 试剂存放位置
    private String drugBuyer; // 试剂购买人
    private String drugSource; // 试剂购买渠道
    private String drugUrl; // 试剂链接
    private LocalDate drugDate; // 试剂入库时间
}
