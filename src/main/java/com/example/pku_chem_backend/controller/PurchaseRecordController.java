package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchaseRecord")
@CrossOrigin(origins = "*")
public class PurchaseRecordController {
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

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

}
