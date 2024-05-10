package com.example.pku_chem_backend.dto;

import com.example.pku_chem_backend.entity.HazardRecord;
import com.example.pku_chem_backend.entity.HazardRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentHazardRecordDTO implements Serializable {
    private HazardRecord hazardRecord;
    private String requesterName;
    private String processorName;
}
