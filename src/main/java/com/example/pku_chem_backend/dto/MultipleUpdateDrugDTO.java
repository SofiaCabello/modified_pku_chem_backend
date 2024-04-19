package com.example.pku_chem_backend.dto;

import com.example.pku_chem_backend.entity.Drug;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleUpdateDrugDTO implements Serializable {
    private Integer[] ids;
    private Drug drug;
}
