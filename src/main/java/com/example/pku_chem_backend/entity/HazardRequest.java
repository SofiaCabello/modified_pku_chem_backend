package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class HazardRequest {
    private Integer id;
    private String type;
    private String location;
    private Timestamp requestDate;
    private String requester;
    private String status;
}
