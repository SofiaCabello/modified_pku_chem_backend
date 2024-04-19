package com.example.pku_chem_backend.dto;

import com.example.pku_chem_backend.entity.Drug;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class GetDrugResultDTO implements Serializable {
    private Long total;
    private List<Drug> data;
}
