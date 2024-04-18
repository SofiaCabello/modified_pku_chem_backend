package com.example.pku_chem_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MergeDictionaryDTO implements Serializable {
    private String tagType;
    private String tag;
    private String targetTag;
}
