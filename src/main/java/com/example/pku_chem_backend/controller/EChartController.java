package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.service.EChartService;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echart")
public class EChartController {
    @Autowired
    private EChartService eChartService;

    @GetMapping("/producerRatio")
    public Result getProducerChart(){
        return Result.ok(eChartService.getProducerCountMap()).message("获取成功");
    }
}
