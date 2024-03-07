package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.Result;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/purchaseRecord")
@CrossOrigin(origins = "*")
public class PurchaseRecordController {
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/getRecord")
    public Result getRecord(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ){
        Page<PurchaseRecord> pageParam = new Page<>(page, limit);
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        purchaseRecordMapper.selectPage(pageParam, wrapper);
        wrapper.orderByDesc("id");
        List<PurchaseRecord> list = pageParam.getRecords();
        return Result.ok(list).total(pageParam.getTotal());
    }

    @GetMapping("/getRecentRecord")
    public Result getRecentRecord(){
        List<PurchaseRecord> list = purchaseRecordMapper.getRecentRecord();
        List<RecordWithExtraData> result = new ArrayList<>();
        for(PurchaseRecord record: list){
            RecordWithExtraData recordWithExtraData = new RecordWithExtraData();
            recordWithExtraData.setId(record.getId());
            recordWithExtraData.setDrugId(record.getDrugId());
            recordWithExtraData.setBuyer(record.getBuyer());
            recordWithExtraData.setProcessor(record.getProcessor());
            recordWithExtraData.setQuantity(record.getQuantity());
            recordWithExtraData.setApproveDate(record.getApproveDate());
            recordWithExtraData.setRequestDate(record.getRequestDate());
            recordWithExtraData.setBuyerName(userMapper.selectByUsername(record.getBuyer()).getRealName());
            recordWithExtraData.setProcessorName(userMapper.selectByUsername(record.getProcessor()).getRealName());
            recordWithExtraData.setDrugName(drugMapper.selectById(record.getDrugId()).getName());
            result.add(recordWithExtraData);
        }
        return Result.ok(result);
    }

    @Getter
    @Setter
    @Data
    @ToString
    private class RecordWithExtraData extends PurchaseRecord {
        private String drugName;
        private String buyerName;
        private String processorName;
    }
}
