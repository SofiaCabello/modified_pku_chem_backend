package com.example.pku_chem_backend.service;

import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseRecordService {
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;


}
