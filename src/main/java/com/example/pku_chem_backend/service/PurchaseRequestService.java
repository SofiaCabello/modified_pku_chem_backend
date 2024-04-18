package com.example.pku_chem_backend.service;

import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseRequestService {
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;

    public void replaceSource(String tag, String targetTag){
        purchaseRequestMapper.replaceSource(tag, targetTag);
    }
}
