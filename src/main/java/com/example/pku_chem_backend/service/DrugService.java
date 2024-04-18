package com.example.pku_chem_backend.service;

import com.example.pku_chem_backend.mapper.DrugMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugService {
    @Autowired
    private DrugMapper drugMapper;

    /**
     * 更新药品的生产商
     * @param tag 要更新的生产商
     * @param targetTag 更新后的生产商
     */
    public void replaceProducer(String tag, String targetTag){
        drugMapper.replaceProducer(tag, targetTag);
    }

    /**
     * 更新药品的实验室
     * @param tag 要更新的实验室
     * @param targetTag 更新后的实验室
     */
    public void replaceLab(String tag, String targetTag){
        drugMapper.replaceLab(tag, targetTag);
    }

    /**
     * 更新药品的位置
     * @param tag 要更新的位置
     * @param targetTag 更新后的位置
     */
    public void replaceLocation(String tag, String targetTag){
        drugMapper.replaceLocation(tag, targetTag);
    }
}
