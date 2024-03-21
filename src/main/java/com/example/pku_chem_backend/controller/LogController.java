package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/log")
@RestController
public class LogController {
    @GetMapping("/createLog")
    public Result createLog() {
//        LogUtil.createLogFileForCurrent();
        return Result.ok().message("日志文件已创建。");
    }
}
