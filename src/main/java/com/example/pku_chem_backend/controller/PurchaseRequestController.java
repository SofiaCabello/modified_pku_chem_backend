package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.entity.PurchaseRequest;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/purchaseRequest")
@CrossOrigin(origins = "*")
public class PurchaseRequestController {
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @PostMapping("/getRequest")
    public Result getRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="token") String token
    ){
        Page<PurchaseRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<PurchaseRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        String username = JwtUtil.getUsername(token);
        wrapper.eq("buyer", username); // 只能看到自己的申请
        purchaseRequestMapper.selectPage(pageParam, wrapper);
        List<PurchaseRequest> list = pageParam.getRecords();
        return Result.ok(list).total(pageParam.getTotal());
    }

    @PostMapping("/addRequest")
    public Result addRequest(@RequestBody PurchaseRequest purchaseRequest){
        LocalDate requestDate = LocalDate.now();
        purchaseRequest.setRequestDate(requestDate.toString());
        purchaseRequestMapper.insert(purchaseRequest);
        return Result.ok().message("申请已提交，等待审批。");
    }

    @PostMapping("/setRequestAsRecord")
    public Result setRequestAsRecord(
            @RequestBody PurchaseRequest purchaseRequest,
            @RequestParam String processorToken) {
        Integer requestId = purchaseRequest.getId();
        PurchaseRequest request = purchaseRequestMapper.selectById(requestId);
        purchaseRequestMapper.deleteById(requestId);
        String processor = JwtUtil.getUsername(processorToken);
        PurchaseRecord record = new PurchaseRecord();
        LocalDate date = LocalDate.now();
        record.setId(requestId);
        record.setBuyer(request.getBuyer());
        record.setSource(request.getSource());
        record.setProcessor(processor);
        record.setApproveDate(date.toString());
        purchaseRecordMapper.insert(record);
        return Result.ok().message("申请已批准，前往试剂页查看记录。");
    }
}
