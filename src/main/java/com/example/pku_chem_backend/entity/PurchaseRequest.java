package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class PurchaseRequest {
    private Integer id;
    private Integer drugId;
    private String source;
    private String buyer;
    private Timestamp requestDate;
    private Integer quantity;
    private String status;
}