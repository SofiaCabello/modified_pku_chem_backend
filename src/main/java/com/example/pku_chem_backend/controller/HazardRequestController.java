package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.HazardRequest;
import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hazardRequest")
@CrossOrigin(origins = "*")
public class HazardRequestController {
    @Autowired
    private HazardRequestMapper hazardRequestMapper;

    @GetMapping("/getRequest")
    public Result getRequest(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="token") String token
    ){
        Page<HazardRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<HazardRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        String username = JwtUtil.getUsername(token);
        wrapper.eq("buyer", username); // 只能看到自己的申请
        hazardRequestMapper.selectPage(pageParam, wrapper);
        List<HazardRequest> list = pageParam.getRecords();
        return Result.ok(list).total(pageParam.getTotal());
    }

}
