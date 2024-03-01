package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.HazardRequest;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.HazardRecordMapper;
import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.Result;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/hazardRequest")
@CrossOrigin(origins = "*")
public class HazardRequestController {
    @Autowired
    private HazardRequestMapper hazardRequestMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getRequest")
    public Result getRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestHeader("Authorization") String token
    ){
        Page<HazardRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<HazardRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        String username = JwtUtil.getUsername(token);
        wrapper.eq("requester", username); // 只能看到自己的申请
        hazardRequestMapper.selectPage(pageParam, wrapper);
        List<HazardRequest> list = pageParam.getRecords();
        return Result.ok(list).total(pageParam.getTotal());
    }

    @PostMapping("/createRequest")
    public Result createRequest(
            @RequestBody HazardRequest hazardRequest,
            @RequestHeader("Authorization") String token) {
        String username = JwtUtil.getUsername(token);
        hazardRequest.setRequestDate(java.time.LocalDate.now().toString());
        hazardRequest.setRequester(username);
        System.out.println(hazardRequest);
        hazardRequestMapper.insertHazardRequest(hazardRequest.getRequester(), hazardRequest.getRequestDate(), hazardRequest.getType(), hazardRequest.getLocation());
        return Result.ok().message("申请已提交，等待审批。");
    }

    @GetMapping("/getAll")
    public Result getAllRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestHeader("Authorization") String token
    ){
        String username = JwtUtil.getUsername(token);
        if(!Objects.equals(userMapper.getRole(username), "admin")){
            return Result.fail().message("非法访问");
        }
        Page<HazardRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<HazardRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        hazardRequestMapper.selectPage(pageParam, wrapper);
        List<HazardRequest> list = pageParam.getRecords();
        List<HazardRequest> newList = new ArrayList<>();
        int total = 0;
        for(HazardRequest request: list){
            if(Objects.equals(request.getStatus(), "pending")){
                User user = userMapper.selectByUsername(request.getRequester());
                HazardRequestWithExtraData requestWithUser = new HazardRequestWithExtraData();
                requestWithUser.setUser(user);
                requestWithUser.setId(request.getId());
                requestWithUser.setRequester(request.getRequester());
                requestWithUser.setRequestDate(request.getRequestDate());
                requestWithUser.setType(request.getType());
                requestWithUser.setLocation(request.getLocation());
                requestWithUser.setStatus(request.getStatus());
                newList.add(requestWithUser);
                total++;
            }
        }
        return Result.ok(newList).total(total);
    }

    @PostMapping("/approve")
    public Result approveRequest(
            @RequestParam Integer requestId,
            @RequestHeader("Authorization") String token
    ){
        String processor = JwtUtil.getUsername(token);
        if(!Objects.equals(userMapper.getRole(processor), "admin")){
            return Result.fail().message("非法访问");
        }
        hazardRequestMapper.updateStatus(requestId, "approved");

        return Result.ok().message("审批成功");
    }

    @PostMapping("/reject")
    public Result rejectRequest(
            @RequestParam Integer requestId,
            @RequestHeader("Authorization") String token
    ){
        String processor = JwtUtil.getUsername(token);
        if(!Objects.equals(userMapper.getRole(processor), "admin")){
            return Result.fail().message("非法访问");
        }
        hazardRequestMapper.updateStatus(requestId, "rejected");
        return Result.ok().message("已拒绝");
    }

    @PostMapping("/setRead")
    public Result setRead(
            @RequestParam Integer requestId,
            @RequestHeader("Authorization") String token
    ){
        String username = JwtUtil.getUsername(token);
        HazardRequest request = hazardRequestMapper.selectById(requestId);
        if(!Objects.equals(request.getRequester(), username)){
            return Result.fail().message("非法访问");
        }
        hazardRequestMapper.deleteById(requestId);
        return Result.ok().message("已标记为已读");
    }

    @Getter
    @Setter
    @Data
    @ToString
    class HazardRequestWithExtraData extends HazardRequest{
        private User user;
    }
}
