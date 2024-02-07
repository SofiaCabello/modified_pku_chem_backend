package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drug")
@CrossOrigin(origins = "*")
public class DrugController {
    @Autowired
    private DrugMapper drugMapper;

    /**
     * 获取药品信息
     * @param page 分页查询的页码
     * @param limit 分页查询的每页数量
     * @param sort  排序规则
     * @param name  药品名称
     * @param producer 生产厂家
     * @param specification 规格
     * @param formula 分子式
     * @param CAS CAS号
     * @param location 存储位置
     * @return
     */
    @GetMapping("/getDrug")
    public Result getDrug(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="sort", defaultValue="+id") String sort,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) String specification,
            @RequestParam(required = false) String formula,
            @RequestParam(required = false) String CAS,
            @RequestParam(required = false) String location
    ){
        Page<Drug> pageParam = new Page<>(page, limit); // 承载分页查询的参数
        QueryWrapper<Drug> wrapper = new QueryWrapper<>(); // 承载查询条件的参数
        switch (sort) {
            case "+id" -> wrapper.orderByAsc("id");
            case "-id" -> wrapper.orderByDesc("id");
            case "+nickName" -> wrapper.orderByAsc("nick_name");
            case "-nickName" -> wrapper.orderByDesc("nick_name");
            case "+stock" -> wrapper.orderByAsc("stock");
            case "-stock" -> wrapper.orderByDesc("stock");
            case "+cas" -> wrapper.orderByAsc("cas");
            case "-cas" -> wrapper.orderByDesc("cas");
            case "+formula" -> wrapper.orderByAsc("formula");
            case "-formula" -> wrapper.orderByDesc("formula");
            case "+specification" -> wrapper.orderByAsc("specification");
            case "-specification" -> wrapper.orderByDesc("specification");
        }
        if(name!=null){
            wrapper.like("name", name);
        }
        if(producer!=null){
            wrapper.like("producer", producer);
        }
        if(specification!=null){
            wrapper.like("specification", specification);
        }
        if(formula!=null){
            wrapper.like("formula", formula);
        }
        if(CAS!=null){
            wrapper.like("CAS", CAS);
        }
        if(location!=null){
            wrapper.like("location", location);
        }
        drugMapper.selectPage(pageParam, wrapper);
        List<Drug> list = pageParam.getRecords();
        return Result.ok(list).total(pageParam.getTotal()).message("获取试剂列表成功");
    }

    /**
     * 创建药品
     * @param drug 药品信息
     * @return 创建成功
     */
    @PostMapping("/createDrug")
    public Result createDrug(@RequestBody Drug drug){
        System.out.println(drug);
        drugMapper.insertDrug(drug.getName(), drug.getProducer(), drug.getSpecification(), drug.getNickName(), drug.getFormula(), drug.getCas(), drug.getLocation(), drug.getUrl(), drug.getStock());
        return Result.ok().message("创建试剂成功");
    }

    @PostMapping("/deleteDrug")
    public Result deleteDrug(@RequestParam(value = "id") Integer id){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        drugMapper.delete(wrapper);
        return Result.ok().message("删除试剂成功");
    }

    @PostMapping("/updateDrug")
    public Result updateDrug(@RequestBody Drug drug){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.eq("id", drug.getId());
        drugMapper.update(drug, wrapper);
        return Result.ok().message("更新试剂成功");
    }
}
