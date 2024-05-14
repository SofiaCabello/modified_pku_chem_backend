package com.example.pku_chem_backend.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@Builder
public class PurchaseRecord {
    private Integer id;
    private Integer drugId;
    private Integer quantity;
    private String buyer;
    private String source;
    private String processor;
    private Timestamp approveDate;
    private Timestamp requestDate;
}
