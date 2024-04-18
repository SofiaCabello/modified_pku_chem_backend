package com.example.pku_chem_backend.service;

import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HazardRequestService {
    @Autowired
    private HazardRequestMapper hazardRequestMapper;

    public void replaceLab(String tag, String targetTag){
        hazardRequestMapper.replaceLab(tag, targetTag);
    }

    public void replaceLocation(String tag, String targetTag){
        hazardRequestMapper.replaceLocation(tag, targetTag);
    }

    public void replaceType(String tag, String targetTag) {
        hazardRequestMapper.replaceType(tag, targetTag);
    }
}
