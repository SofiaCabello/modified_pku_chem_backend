package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class Drug {
    private Integer id;
    private String name;
    private String producer;
    private String specification;
    private String nickName;
    private String formula;
    private String cas;
    private String lab;
    private String location;
    private Integer layer;
    private String url;
    private String stock;
    private String note;
}
