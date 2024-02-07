package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseRecord {
    private Integer id;
    private Integer drugId;
    private Integer quantity;
    private String buyer;
    private String source;
    private String processor;
    private String approveDate;
    private String requestDate;
}
