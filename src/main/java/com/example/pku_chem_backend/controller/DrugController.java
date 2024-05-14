package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.dto.GetDrugDTO;
import com.example.pku_chem_backend.dto.GetDrugResultDTO;
import com.example.pku_chem_backend.dto.MultipleUpdateDrugDTO;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
import com.example.pku_chem_backend.service.DrugService;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/drug")
@CrossOrigin(origins = "*")
public class DrugController {
    @Autowired
    private DrugService drugService;

    /**
     * 获取药品信息
     * @param DTO 获取药品的DTO
     */
    @GetMapping("/getDrug")
    public Result getDrug(@ModelAttribute GetDrugDTO DTO){ // @ModelAttribute 注解用于绑定查询参数
        GetDrugResultDTO result = drugService.getDrug(DTO, false);
        return Result.ok(result.getData()).total(result.getTotal()).message("获取成功");
    }

    @GetMapping("/getAll")
    public Result getAll(@ModelAttribute GetDrugDTO DTO){
        GetDrugResultDTO result = drugService.getDrug(DTO, true);
        return Result.ok(result.getData()).total(result.getTotal()).message("获取成功");
    }

    /**
     * 创建药品
     * @param drug 药品信息
     * @return 创建成功
     */
    @PostMapping("/createDrug")
    public Result createDrug(@RequestBody Drug drug){
        try{
            drugService.createDrug(drug);
            return Result.ok().message("创建成功");
        } catch (Exception e){
            return Result.fail().message("创建失败");
        }
    }

    @PostMapping("/deleteDrug")
    public Result deleteDrug(@RequestParam(value = "id") Integer id){
        try{
            drugService.deleteDrug(id);
            return Result.ok().message("删除成功");
        } catch (Exception e){
            return Result.fail().message("删除失败");
        }
    }

    @PostMapping("/updateDrug")
    public Result updateDrug(@RequestBody Drug drug){
        try{
            drugService.updateDrug(drug);
            return Result.ok().message("更新成功");
        } catch (Exception e){
            return Result.fail().message("更新失败");
        }
    }

    // TODO: 将该功能迁移到PurchaseRecordController

//    @GetMapping("/getRecord")
//    public Result getRecord(@RequestParam(value = "id") Integer id){
//        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("drug_id", id);
//        List<PurchaseRecord> list = null;
//        try {
//            list = purchaseRecordMapper.selectList(wrapper);
//        } catch (Exception e) {
//            return Result.fail().message("获取记录失败");
//        }  // 获取记录失败
//        return Result.ok(list).message("获取记录成功");
//    }

    @PostMapping("/multipleUpdateDrug")
    public Result multipleUpdateDrug(@RequestBody MultipleUpdateDrugDTO DTO){
        try{
            drugService.multipleUpdateDrug(DTO);
            return Result.ok().message("更新成功");
        } catch (Exception e){
            return Result.fail().message("更新失败");
        }
    }
}
