package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseRequest {
    private Integer id;
    private Integer drug_id;
    private String source;
    private String buyer;
    private String requestDate;
}