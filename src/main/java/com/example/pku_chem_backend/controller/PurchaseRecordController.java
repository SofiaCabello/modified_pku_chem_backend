package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.service.PurchaseRecordService;
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
    private PurchaseRecordService purchaseRecordService;

    @GetMapping("/getRecord")
    public Result getRecord(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "id") Integer drug_id
    ){
        try {
            return Result.ok(purchaseRecordService.getRecord(page, limit, drug_id)).message("获取成功");
        } catch (Exception e) {
            return Result.fail().message("获取失败");
        }
    }

    @GetMapping("/getRecentRecord")
    public Result getRecentRecord(){
        try {
            return Result.ok(purchaseRecordService.getRecentRecord()).message("获取成功");
        } catch (Exception e) {
            return Result.fail().message("获取失败");
        }
    }
}
