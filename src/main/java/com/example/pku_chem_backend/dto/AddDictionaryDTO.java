package com.example.pku_chem_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AddDictionaryDTO implements Serializable {
    private String tagType;
    private String tag;
}
