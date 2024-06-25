package com.example.pku_chem_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class PurchaseRequest {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer drugId;
    private String source;
    private String buyer;
    private Timestamp requestDate;
    private Integer quantity;
    private String status;
}