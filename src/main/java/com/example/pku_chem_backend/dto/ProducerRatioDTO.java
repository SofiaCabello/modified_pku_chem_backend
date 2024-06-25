package com.example.pku_chem_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProducerRatioDTO implements Serializable {
    private Integer value;
    private String name;
}

