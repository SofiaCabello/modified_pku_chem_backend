package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.dto.GetRequestDTO;
import com.example.pku_chem_backend.entity.HazardRequest;
import com.example.pku_chem_backend.mapper.HazardRecordMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.service.HazardRequestService;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hazardRequest")
@CrossOrigin(origins = "*")
public class HazardRequestController {
    @Autowired
    private HazardRequestService hazardRequestService;

    @GetMapping("/getRequest")
    public Result getRequest(@ModelAttribute GetRequestDTO getRequestDTO, @RequestHeader("Authorization") String token){
        try{
            return Result.ok(hazardRequestService.getRequest(getRequestDTO, token)).message("获取成功");
        }
        catch (Exception e){
            return Result.fail().message("获取失败");
        }
    }

    @PostMapping("/createRequest")
    public Result createRequest(@RequestBody HazardRequest hazardRequest) {
        if(hazardRequestService.createRequest(hazardRequest)){
            return Result.ok().message("申请成功");
        } else {
            return Result.fail().message("申请失败");
        }
    }

    @GetMapping("/getAll")
    public Result getAllRequest(@RequestParam(value="page", defaultValue="1") Integer page, @RequestParam(value="limit", defaultValue="10") Integer limit){
        try{
            return Result.ok(hazardRequestService.getAllRequest(page, limit)).message("获取成功");
        }
        catch (Exception e){
            return Result.fail().message("获取失败");
        }
    }

    @PostMapping("/approve")
    public Result approveRequest(@RequestParam Integer requestId){
        if(hazardRequestService.processRequest(requestId, true)){
            return Result.ok().message("已批准");
        } else {
            return Result.fail().message("批准失败");
        }
    }

    @PostMapping("/reject")
    public Result rejectRequest(@RequestParam Integer requestId){
        if(hazardRequestService.processRequest(requestId, false)){
            return Result.ok().message("已拒绝");
        } else {
            return Result.fail().message("拒绝失败");
        }
    }

    @PostMapping("/setRead")
    public Result setRead(@RequestParam Integer requestId){
        if(hazardRequestService.deleteRequest(requestId)){
            return Result.ok().message("已标记为已读");
        } else {
            return Result.fail().message("标记失败");
        }
    }

    @GetMapping("/getRecent")
    public Result getRecentRequest(){
        try{
            return Result.ok(hazardRequestService.getRecentRecord()).message("获取成功");
        }
        catch (Exception e){
            return Result.fail().message("获取失败");
        }
    }
}
