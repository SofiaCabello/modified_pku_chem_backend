package com.example.pku_chem_backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class HazardRequest {
    @TableId(value = "id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer id;
    private String type;
    private String location;
    private Timestamp requestDate;
    private String requester;
    private String status;
}
