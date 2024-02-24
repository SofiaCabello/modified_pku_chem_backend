package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseRequest {
    private Integer id;
    private Integer drugId;
    private String source;
    private String buyer;
    private String requestDate;
    private Integer quantity;
    private String status;
}