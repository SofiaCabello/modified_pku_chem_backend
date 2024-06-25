package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.dto.PurchaseRequestDTO;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.entity.PurchaseRequest;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseRequestService {
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;
    @Autowired
    private DrugService drugService;
    @Autowired
    private PurchaseRecordService purchaseRecordService;

    public void replaceSource(String tag, String targetTag){
        purchaseRequestMapper.replaceSource(tag, targetTag);
    }

    public List<PurchaseRequestDTO> getPurchaseRequest(Integer id, Integer limit, String token) {
        Page<PurchaseRequest> pageParam = new Page<>(id, limit);
        QueryWrapper<PurchaseRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.eq("buyer", JwtUtil.getUsername(token));
        return getPurchaseRequestDTOS(pageParam, wrapper);
    }

    public boolean addPurchaseRequest(PurchaseRequest purchaseRequest){
        purchaseRequest.setId(null);
        purchaseRequest.setStatus("pending");
        return purchaseRequestMapper.insert(purchaseRequest) > 0;
    }

    public List<PurchaseRequestDTO> getAllPurchaseRequest(Integer page, Integer limit){
        Page<PurchaseRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<PurchaseRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.eq("status", "pending");
        return getPurchaseRequestDTOS(pageParam, wrapper);
    }

    public void processPurchaseRequest(Integer id, boolean approve, String processor){
        PurchaseRequest purchaseRequest = purchaseRequestMapper.selectById(id);
        if(approve){
            purchaseRequest.setStatus("approved");
            purchaseRequestMapper.updateById(purchaseRequest);
            PurchaseRecord purchaseRecord = PurchaseRecord.builder()
                    .id(id)
                    .drugId(purchaseRequest.getDrugId())
                    .quantity(purchaseRequest.getQuantity())
                    .buyer(purchaseRequest.getBuyer())
                    .source(purchaseRequest.getSource())
                    .requestDate(purchaseRequest.getRequestDate())
                    .processor(processor)
                    .build();
            purchaseRecordService.addPurchaseRecord(purchaseRecord);
        } else {
            purchaseRequest.setStatus("rejected");
            purchaseRequestMapper.updateById(purchaseRequest);
        }
    }

    public boolean deletePurchaseRequest(Integer id){
        return purchaseRequestMapper.deleteById(id) > 0;
    }

    private List<PurchaseRequestDTO> getPurchaseRequestDTOS(Page<PurchaseRequest> pageParam, QueryWrapper<PurchaseRequest> wrapper) {
        purchaseRequestMapper.selectPage(pageParam, wrapper);
        List<PurchaseRequest> list = pageParam.getRecords();

        List<PurchaseRequestDTO> result = new ArrayList<>();
        for(PurchaseRequest request: list){
            Drug drug = drugService.getDrugById(request.getDrugId());
            PurchaseRequestDTO requestDTO = PurchaseRequestDTO.builder()
                    .purchaseRequest(request)
                    .drug(drug)
                    .build();
            result.add(requestDTO);
        }
        return result;
    }

}
