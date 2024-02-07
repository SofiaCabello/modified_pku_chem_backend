package com.example.pku_chem_backend.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pku_chem_backend.entity.Dictionary;
import com.example.pku_chem_backend.mapper.DictionaryMapper;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/dictionary")
@CrossOrigin(origins = "*")
public class DictionaryController {
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
    public Result deleteDictionary(@RequestParam String tagType, @RequestParam String tag){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", tagType);
        wrapper.eq("options", tag);
        dictionaryMapper.delete(wrapper);
        return Result.ok().message("删除成功");
    }

    @PostMapping("/add")
    public Result addDictionary(@RequestParam String tagType, @RequestParam String tag){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", tagType);
        wrapper.eq("options", tag);
        if(dictionaryMapper.selectOne(wrapper) != null){
            return Result.fail().message("添加失败，已存在相同标签");
        }
        dictionaryMapper.insertDictionary(tagType, tag);
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
            @RequestParam String targetTag){
        switch (tagType){
            case "producerTags":
                drugMapper.replaceProducer(tag, targetTag);
                break;
            case "labTags", "locationTags":
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
        return Result.ok();
    }
}
