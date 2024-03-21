package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pku_chem_backend.entity.Dictionary;
import com.example.pku_chem_backend.mapper.DictionaryMapper;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/dictionary")
@CrossOrigin(origins = "*")
public class DictionaryController {
    private LogUtil logUtil = new LogUtil();
    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;
    @Autowired
    private HazardRequestMapper hazardRequestMapper;

    @GetMapping("/get")
    public Result getDictionary(){
        List<String> producerList = dictionaryMapper.getOptionsByType("producerTags");
        List<String> labList = dictionaryMapper.getOptionsByType("labTags");
        List<String> locationList = dictionaryMapper.getOptionsByType("locationTags");
        List<String> sourceList = dictionaryMapper.getOptionsByType("sourceTags");
        List<String> wasteList = dictionaryMapper.getOptionsByType("wasteTags");
        Map<String, List<String>> dictionary = new HashMap<>();
        dictionary.put("producerTags", producerList);
        dictionary.put("labTags", labList);
        dictionary.put("locationTags", locationList);
        dictionary.put("sourceTags", sourceList);
        dictionary.put("wasteTags", wasteList);
        return Result.ok(dictionary);
    }

    @PostMapping("/delete")
    public Result deleteDictionary(@RequestParam String tagType, @RequestParam String tag, HttpServletRequest request){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", tagType);
        wrapper.eq("options", tag);
        dictionaryMapper.delete(wrapper);
        logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "DELETE", "删除字典项" + tagType + ":" + tag, new Date().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("删除成功");
    }

    @PostMapping("/add")
    public Result addDictionary(@RequestParam String tagType, @RequestParam String tag, HttpServletRequest request){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", tagType);
        wrapper.eq("options", tag);
        if(dictionaryMapper.selectOne(wrapper) != null){
            return Result.fail().message("添加失败，已存在相同标签");
        }
        dictionaryMapper.insertDictionary(tagType, tag);
        logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "CREATE", "添加字典项" + tagType + ":" + tag, new Date().toString(), request.getRemoteAddr(), request);
        return Result.ok().message("添加成功");
    }

    @GetMapping("/getWaste")
    public Result getWasteDictionary(){
        List<String> wasteList = dictionaryMapper.getOptionsByType("wasteTags");
        return Result.ok(wasteList);
    }

    @GetMapping("/getLocation")
    public Result getLocationDictionary(){
        List<String> locationList = dictionaryMapper.getOptionsByType("locationTags");
        return Result.ok(locationList);
    }

    @GetMapping("/getLab")
    public Result getLabDictionary(){
        List<String> labList = dictionaryMapper.getOptionsByType("labTags");
        return Result.ok(labList);
    }

    @PostMapping("/merge")
    public Result mergeDictionary(
            @RequestParam String tagType,
            @RequestParam String tag,
            @RequestParam String targetTag,
            HttpServletRequest request){
        switch (tagType){
            case "producerTags":
                drugMapper.replaceProducer(tag, targetTag);
                break;
            case "labTags":
                drugMapper.replaceLab(tag, targetTag);
                hazardRequestMapper.replaceLab(tag, targetTag);
                break;
            case "locationTags":
                drugMapper.replaceLocation(tag, targetTag);
                hazardRequestMapper.replaceLocation(tag, targetTag);
                break;
            case "sourceTags":
                purchaseRequestMapper.replaceSource(tag, targetTag);
                break;
            case "wasteTags":
                hazardRequestMapper.replaceType(tag, targetTag);
                break;
        }
        logUtil.writeLog(JwtUtil.getUsername(request.getHeader("Authorization")), "UPDATE", "合并字典项" + tagType + ":" + tag + "=>" + targetTag, new Date().toString(), request.getRemoteAddr(), request);
        return Result.ok();
    }
}
