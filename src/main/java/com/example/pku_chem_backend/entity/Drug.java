package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;
import nonapi.io.github.classgraph.json.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Data
@ToString
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer stock;
    private String note;
}
