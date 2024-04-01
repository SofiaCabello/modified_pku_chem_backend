package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import com.example.pku_chem_backend.mapper.UserMapper;
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
    private LogUtil logUtil = new LogUtil();
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取药品信息
     * @param page 分页查询的页码
     * @param limit 分页查询的每页数量
     * @param sort  排序规则
     * @param name  药品名称
     * @param producer 生产厂家
     * @param specification 规格
     * @param formula 分子式
     * @param cas CAS号
     * @param location 存储位置
     * @return
     */
    @GetMapping("/getDrug")
    public Result getDrug(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="sort", defaultValue="+id") String sort,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) String specification,
            @RequestParam(required = false) String formula,
            @RequestParam(required = false) String cas,
            @RequestParam(required = false) String lab,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String layer,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String realName
    ){
        Page<Drug> pageParam = new Page<>(page, limit); // 承载分页查询的参数
        QueryWrapper<Drug> wrapper = new QueryWrapper<>(); // 承载查询条件的参数
        switch (sort) {
            case "+id" -> wrapper.orderByAsc("id");
            case "-id" -> wrapper.orderByDesc("id");
            case "+stock" -> wrapper.orderByAsc("stock");
            case "-stock" -> wrapper.orderByDesc("stock");
            case "+cas" -> wrapper.orderByAsc("cas");
            case "-cas" -> wrapper.orderByDesc("cas");
            case "+formula" -> wrapper.orderByAsc("formula");
            case "-formula" -> wrapper.orderByDesc("formula");
            case "+specification" -> wrapper.orderByAsc("specification");
            case "-specification" -> wrapper.orderByDesc("specification");
            case "+producer" -> wrapper.orderByAsc("CONVERT(producer USING gbk)");
            case "-producer" -> wrapper.orderByDesc("CONVERT(producer USING gbk)");
            case "+name" -> wrapper.orderByAsc("CONVERT(name USING gbk)");
            case "-name" -> wrapper.orderByDesc("CONVERT(name USING gbk)");
            case "+lab" -> wrapper.orderByAsc("lab").orderByAsc("location").orderByAsc("layer");
            case "-lab" -> wrapper.orderByDesc("lab").orderByDesc("location").orderByDesc("layer");
        }
        setQuery(id, name, producer, specification, formula, cas, lab, location, layer, note, realName, wrapper);
        List<Integer> ids = purchaseRecordMapper.getDrugIdByBuyer(userMapper.getUsernameByRealName(realName));
        if(!ids.isEmpty()){
            wrapper.in("id", ids);
        }
        drugMapper.selectPage(pageParam, wrapper);
        List<Drug> list = pageParam.getRecords();
        return Result.ok(list).total(pageParam.getTotal());
    }

    @GetMapping("/getAll")
    public Result getAll(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) String specification,
            @RequestParam(required = false) String formula,
            @RequestParam(required = false) String cas,
            @RequestParam(required = false) String lab,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String layer,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String realName
    ){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>(); // 承载查询条件的参数
        setQuery(id, name, producer, specification, formula, cas, lab, location, layer, note, realName, wrapper);

        List<Drug> list = drugMapper.selectList(wrapper);
        return Result.ok(list).message("获取试剂列表成功");
    }

    private void setQuery(@RequestParam(required = false) Integer id,@RequestParam(required = false) String name, @RequestParam(required = false) String producer, @RequestParam(required = false) String specification, @RequestParam(required = false) String formula, @RequestParam(required = false) String cas, @RequestParam(required = false) String lab, @RequestParam(required = false) String location, @RequestParam(required = false) String layer, @RequestParam(required = false) String note, @RequestParam(required = false) String realName,QueryWrapper<Drug> wrapper) {
        if(id!=null){
            wrapper.eq("id", id);
        }
        if(name!=null){
            wrapper.like("name", name).or().like("nick_name", name);
        }
        if(producer!=null){
            wrapper.like("producer", producer);
        }
        if(specification!=null){
            wrapper.like("specification", specification);
        }
        if(formula!=null){
            List<String> elements = splitFormula(formula);
            for(String element: elements){
                wrapper.like("formula", "%"+element+"%"); // 用于模糊查询
            }
        }
        if(cas!=null){
            wrapper.like("cas", cas);
        }
        if(lab!=null){
            wrapper.like("lab", lab);
        }
        if(location!=null){
            wrapper.like("location", location);
        }
        if(layer!=null){
            wrapper.like("layer", layer);
        }
        if(note!=null){
            wrapper.like("note", note);
        }
    }

    /**
     * 创建药品
     * @param drug 药品信息
     * @return 创建成功
     */
    @PostMapping("/createDrug")
    public Result createDrug(
            @RequestBody Drug drug,
            HttpServletRequest request
    ){
        try {
            drugMapper.insertDrug(drug.getName(), drug.getProducer(), drug.getSpecification(), drug.getNickName(), drug.getFormula(), drug.getCas(), drug.getLab(), drug.getLocation(), drug.getLayer(), drug.getUrl(), drug.getStock(),drug.getNote());
            logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "CREATE", "创建试剂：" + drug.getName(), java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        } catch(Exception e){
            e.printStackTrace();
            return Result.fail().message("创建试剂失败");
        }
        return Result.ok().message("创建试剂成功");
    }

    @PostMapping("/deleteDrug")
    public Result deleteDrug(
            @RequestParam(value = "id") Integer id,
            HttpServletRequest request
    ){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        try {
            String drugName = drugMapper.selectOne(wrapper).getName();
            drugMapper.delete(wrapper);
            logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "DELETE", "删除试剂：" + drugName, java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        } catch (Exception e) {
            return Result.fail().message("删除试剂失败");
        }
        return Result.ok().message("删除试剂成功");
    }

    @PostMapping("/updateDrug")
    public Result updateDrug(
            @RequestBody Drug drug,
            HttpServletRequest request
    ){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.eq("id", drug.getId());
        try {
            drugMapper.update(drug, wrapper);
            logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "UPDATE", "更新试剂：" + drug.getName(), java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        } catch (Exception e) {
            return Result.fail().message("更新试剂失败");
        }
        return Result.ok().message("更新试剂成功");
    }

    @GetMapping("/getRecord")
    public Result getRecord(@RequestParam(value = "id") Integer id){
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("drug_id", id);
        List<PurchaseRecord> list = null;
        try {
            list = purchaseRecordMapper.selectList(wrapper);
        } catch (Exception e) {
            return Result.fail().message("获取记录失败");
        }  // 获取记录失败
        return Result.ok(list).message("获取记录成功");
    }

    @PostMapping("/multipleUpdateDrug")
    public Result multipleUpdateDrug(
            @RequestBody DrugWithIdArray drugWithIdArray,
            HttpServletRequest request
    ){
        Integer[] ids = drugWithIdArray.getIds();
        // 如果字段为空则不更新，如果字段非空则更新对应字段
        // 遍历DrugWithIdArray的所有字段，如果字段非空则更新对应字段
        Class<?> objClass = drugWithIdArray.getClass();
        while(objClass != null){
            Field[] fields = objClass.getDeclaredFields();
            for(Field field: fields){
                field.setAccessible(true);
                try {
                    Object filedValue = field.get(drugWithIdArray);
                    if(filedValue != null && !field.getName().equals("ids") && !(filedValue instanceof String && ((String) filedValue).isEmpty())){
                        String fieldName;
                        if(field.getName().equals("nickName")){
                            fieldName = "nick_name";
                        }
                        else{
                            fieldName = field.getName();
                        }
                        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
                        for(Integer id: ids){
                            wrapper.eq("id", id);
                            drugMapper.updateField(fieldName, field.get(drugWithIdArray).toString(), id);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return Result.fail().message("更新失败");
                }
            }
            objClass = objClass.getSuperclass();
        }
        logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "UPDATE", "批量更新试剂，id：" + ids.toString(), java.time.LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("更新成功");
    }


    public static List<String> splitFormula(String formula) {
        List<String> elements = new ArrayList<>();
        Pattern pattern = Pattern.compile("([A-Z][a-z]*)(\\d*)");
        Matcher matcher = pattern.matcher(formula);

        while (matcher.find()) {
            String element = matcher.group(1);
            String quantity = matcher.group(2).isEmpty() ? "" : matcher.group(2);
            elements.add(element + quantity);
        }
        return elements;
    }


    @Setter
    @Getter
    @ToString
    private static class DrugWithIdArray extends Drug{
        private Integer[] ids;
    }
}
