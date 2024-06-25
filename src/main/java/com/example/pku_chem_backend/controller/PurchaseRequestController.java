package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.dto.PurchaseRequestDTO;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.entity.PurchaseRequest;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.service.PurchaseRequestService;
import com.example.pku_chem_backend.service.UserService;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/purchaseRequest")
@CrossOrigin(origins = "*")
public class PurchaseRequestController {
    @Autowired
    private PurchaseRequestService purchaseRequestService;
    @Autowired
    private UserService userService;

    @GetMapping("/getRequest")
    public Result getRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestHeader("Authorization") String token
    ){
        try{
            return Result.ok(purchaseRequestService.getPurchaseRequest(page, limit, token)).message("获取申请成功");
        } catch (Exception e){
            return Result.fail().message("获取申请失败");
        }
    }

    @PostMapping("/addRequest")
    public Result addRequest(@RequestBody PurchaseRequest purchaseRequest, @RequestHeader("Authorization") String token){
        String username = JwtUtil.getUsername(token);
        purchaseRequest.setBuyer(username);
        purchaseRequest.setDrugId(purchaseRequest.getId());
        if(purchaseRequestService.addPurchaseRequest(purchaseRequest)){
            return Result.ok().message("申请成功");
        } else {
            return Result.fail().message("申请失败");
        }
    }

//    // TODO:修正前端代码 有效性存疑
//    @PostMapping("/setRequestAsRecord")
//    public Result setRequestAsRecord(
//            @RequestBody PurchaseRequest purchaseRequest,
//            @RequestParam String processorToken,
//            HttpServletRequest httpRequest) {
//        Integer requestId = purchaseRequest.getId();
//        PurchaseRequest request = purchaseRequestMapper.selectById(requestId);
//        purchaseRequestMapper.deleteById(requestId);
//        String processor = JwtUtil.getUsername(processorToken);
//        PurchaseRecord record = new PurchaseRecord();
//        LocalDate date = LocalDate.now();
//        record.setId(requestId);
//        record.setBuyer(request.getBuyer());
//        record.setSource(request.getSource());
//        record.setProcessor(processor);
//        record.setApproveDate(date.toString());
//        purchaseRecordMapper.insert(record);
//        logUtil.writeLog(processor, "CREATE", "创建试剂记录", java.time.LocalDateTime.now().toString(), httpRequest.getRemoteAddr(), httpRequest);
//        return Result.ok().message("申请已批准，前往试剂页查看记录。");
//    }

    @GetMapping("/getAll")
    public Result getAllRequest(@RequestParam(value="page", defaultValue="1") Integer page, @RequestParam(value="limit", defaultValue="10") Integer limit) {
        List<PurchaseRequestDTO> result = purchaseRequestService.getAllPurchaseRequest(page, limit);
        for(PurchaseRequestDTO requestDTO : result){
            String buyer = requestDTO.getPurchaseRequest().getBuyer();
            String realName = userService.getRealName(buyer);
            requestDTO.setRealName(realName);

        }
        int total = result.size();
        return Result.ok(result).total(total).message("获取所有申请成功");
    }

    @PostMapping("/approve")
    public Result approveRequest(@RequestParam Integer requestId, @RequestHeader("Authorization") String token){
        String processor = JwtUtil.getUsername(token);
        try {
            purchaseRequestService.processPurchaseRequest(requestId, true, processor);
            return Result.ok().message("批准成功");
        } catch (Exception e){
            e.printStackTrace();
            return Result.fail().message("批准失败");
        }
    }

    @PostMapping("/reject")
    public Result rejectRequest(@RequestParam Integer requestId, @RequestHeader("Authorization") String token){
        String processor = JwtUtil.getUsername(token);
        try {
            purchaseRequestService.processPurchaseRequest(requestId, false, processor);
            return Result.ok().message("拒绝成功");
        } catch (Exception e){
            return Result.fail().message("拒绝失败");
        }
    }

    @PostMapping("/setRead")
    public Result setRead(@RequestParam Integer requestId, @RequestHeader("Authorization") String token){
        if(purchaseRequestService.deletePurchaseRequest(requestId)){
            return Result.ok().message("已读");
        } else {
            return Result.fail().message("删除失败");
        }
    }
}