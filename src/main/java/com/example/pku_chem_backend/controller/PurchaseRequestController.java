package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.entity.PurchaseRequest;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/purchaseRequest")
@CrossOrigin(origins = "*")
public class PurchaseRequestController {
    private LogUtil logUtil = new LogUtil();
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getRequest")
    public Result getRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestHeader("Authorization") String token
    ){
        Page<PurchaseRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<PurchaseRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        String username = JwtUtil.getUsername(token);
        wrapper.eq("buyer", username); // 只能看到自己的申请
        purchaseRequestMapper.selectPage(pageParam, wrapper);
        List<PurchaseRequest> list = pageParam.getRecords();
        for(PurchaseRequest request: list){
            Drug drug = drugMapper.selectById(request.getDrugId());
            purchaseRequestWithExtraData requestWithDrug = new purchaseRequestWithExtraData();
            requestWithDrug.setDrug(drug);
            requestWithDrug.setId(request.getId());
            requestWithDrug.setBuyer(request.getBuyer());
            requestWithDrug.setRequestDate(request.getRequestDate());
            requestWithDrug.setQuantity(request.getQuantity());
            requestWithDrug.setSource(request.getSource());
            requestWithDrug.setStatus(request.getStatus());
            list.set(list.indexOf(request), requestWithDrug);
        }
        return Result.ok(list).total(pageParam.getTotal());
    }

    @PostMapping("/addRequest")
    public Result addRequest(
            @RequestBody PurchaseRequest purchaseRequest,
            @RequestHeader("Authorization") String token,
            HttpServletRequest request){
        LocalDate requestDate = LocalDate.now();
        String username = JwtUtil.getUsername(token);
        purchaseRequest.setBuyer(username);
        purchaseRequest.setRequestDate(requestDate.toString());
        purchaseRequest.setDrugId(purchaseRequest.getId());
        purchaseRequest.setId(null);
        purchaseRequestMapper.insertPurchaseRequest(purchaseRequest.getDrugId(), purchaseRequest.getSource(),purchaseRequest.getBuyer(), purchaseRequest.getRequestDate(), purchaseRequest.getQuantity(), "pending");
        logUtil.writeLog(username, "CREATE", "创建试剂申请", java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("申请已提交，等待审批。");
    }

    @PostMapping("/setRequestAsRecord")
    public Result setRequestAsRecord(
            @RequestBody PurchaseRequest purchaseRequest,
            @RequestParam String processorToken,
            HttpServletRequest httpRequest) {
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
        logUtil.writeLog(processor, "CREATE", "创建试剂记录", java.time.LocalDateTime.now().toString(), httpRequest.getRemoteAddr(), httpRequest);
        return Result.ok().message("申请已批准，前往试剂页查看记录。");
    }

    @GetMapping("/getAll")
    public Result getAllRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestHeader("Authorization") String token
    ) {
        String username = JwtUtil.getUsername(token);
        if(!Objects.equals(userMapper.getRole(username), "admin")){
            return Result.fail().message("非法访问");
        }
        Page<PurchaseRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<PurchaseRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        purchaseRequestMapper.selectPage(pageParam, wrapper);
        List<PurchaseRequest> list = pageParam.getRecords();
        List<PurchaseRequest> resultList = new ArrayList<>();
        int total = 0;
        for(PurchaseRequest request: list) {
            if(Objects.equals(request.getStatus(), "pending")) {
                Drug drug = drugMapper.selectById(request.getDrugId());
                User requester = userMapper.selectByUsername(request.getBuyer());
                purchaseRequestWithExtraData requestWithDrug = new purchaseRequestWithExtraData();
                requestWithDrug.setDrug(drug);
                requestWithDrug.setId(request.getId());
                requestWithDrug.setBuyer(request.getBuyer());
                requestWithDrug.setRequestDate(request.getRequestDate());
                requestWithDrug.setQuantity(request.getQuantity());
                requestWithDrug.setSource(request.getSource());
                requestWithDrug.setUser(requester);
                requestWithDrug.setStatus(request.getStatus());
                resultList.add(requestWithDrug);
                total++;
            }
        }
        return Result.ok(resultList).total(total);
    }

    @PostMapping("/approve")
    public Result approveRequest(
            @RequestParam Integer requestId,
            @RequestHeader("Authorization") String token,
            HttpServletRequest httpRequest
    ){
        String processor = JwtUtil.getUsername(token);
        if(!Objects.equals(userMapper.getRole(processor), "admin")){
            return Result.fail().message("非法访问");
        }
        String approveDate = LocalDate.now().toString();
        PurchaseRequest request = purchaseRequestMapper.selectById(requestId);
        purchaseRecordMapper.insertRecord(requestId, request.getDrugId(), request.getBuyer(), request.getSource(), processor, approveDate, request.getRequestDate(), request.getQuantity());
        purchaseRequestMapper.updateStatus(requestId, "approved");
        logUtil.writeLog(processor, "APPROVE", "审批试剂申请", java.time.LocalDateTime.now().toString(), httpRequest.getRemoteAddr(), httpRequest);
        return Result.ok().message("审批成功");
    }

    @PostMapping("/reject")
    public Result rejectRequest(
            @RequestParam Integer requestId,
            @RequestHeader("Authorization") String token,
            HttpServletRequest httpRequest
    ){
        String processor = JwtUtil.getUsername(token);
        if(!Objects.equals(userMapper.getRole(processor), "admin")){
            return Result.fail().message("非法访问");
        }
        purchaseRequestMapper.updateStatus(requestId, "rejected");
        logUtil.writeLog(processor, "REJECT", "拒绝试剂申请", java.time.LocalDateTime.now().toString(), httpRequest.getRemoteAddr(), httpRequest);
        return Result.ok().message("驳回成功");
    }

    @PostMapping("/setRead")
    public Result setRead(
            @RequestParam Integer requestId,
            @RequestHeader("Authorization") String token,
            HttpServletRequest httpRequest
    ){
        String username = JwtUtil.getUsername(token);
        PurchaseRequest request = purchaseRequestMapper.selectById(requestId);
        if(!Objects.equals(request.getBuyer(), username)){
            return Result.fail().message("非法访问");
        }
        purchaseRequestMapper.deleteById(requestId);
        logUtil.writeLog(username, "READ", "标记试剂申请为已读", java.time.LocalDateTime.now().toString(), httpRequest.getRemoteAddr(), httpRequest);
        return Result.ok();
    }

    @Getter
    @Data
    @ToString
    @Setter
    class purchaseRequestWithExtraData extends PurchaseRequest{
        private Drug drug;
        private User user;
    }
}