package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HazardRecord {
    private Integer id;
    private String type;
    private String location;
    private String requestDate;
    private String requester;
    private String approveDate;
    private String processor;
}
