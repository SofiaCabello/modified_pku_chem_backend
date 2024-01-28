package com.example.pku_chem_backend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseRequest {
    private Integer id;
    private String buyer;
    private String source;
    private String requestDate;
}
