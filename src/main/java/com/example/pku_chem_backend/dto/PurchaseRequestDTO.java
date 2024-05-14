package com.example.pku_chem_backend.dto;

import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.entity.PurchaseRequest;
import com.example.pku_chem_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDTO implements Serializable {
    private PurchaseRequest purchaseRequest;
    private Drug drug;
}
