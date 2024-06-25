package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.dto.AddDictionaryDTO;
import com.example.pku_chem_backend.dto.DeleteDictionaryDTO;
import com.example.pku_chem_backend.dto.GetDictionaryDTO;
import com.example.pku_chem_backend.dto.MergeDictionaryDTO;
import com.example.pku_chem_backend.mapper.DrugMapper;
import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import com.example.pku_chem_backend.mapper.PurchaseRequestMapper;
import com.example.pku_chem_backend.service.DictionaryService;
import com.example.pku_chem_backend.util.LogUtil;
import com.example.pku_chem_backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dictionary")
@CrossOrigin(origins = "*")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 获取字典
     * @return 字典列表
     */
    @GetMapping("/get")
    public Result getDictionary(){
        Map<String, List<String>> result = dictionaryService.getDictionary();
        return Result.ok(result).message("获取成功");
    }

    /**
     * 删除字典项
     * @param DTO 删除字典项的DTO
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public Result deleteDictionary(@RequestParam String tagType, @RequestParam String tag){
        if(dictionaryService.deleteDictionary(new DeleteDictionaryDTO(tagType, tag))){
            return Result.ok().message("删除成功");
        } else {
            return Result.fail().message("删除失败");
        }
    }

    /**
     * 添加字典项
     * @param DTO 添加字典项的DTO
     * @return 是否添加成功
     */
    @PostMapping("/add")
    public Result addDictionary(@RequestBody AddDictionaryDTO DTO){
        if(dictionaryService.addDictionary(DTO)){
            return Result.ok().message("添加成功");
        } else {
            return Result.fail().message("添加失败");
        }
    }

    @GetMapping("/getWaste")
    public Result<List<String>> getWasteDictionary(){
        return Result.ok(dictionaryService.getWasteDictionary()).message("获取成功");
    }

    @GetMapping("/getLocation")
    public Result getLocationDictionary(){
        return Result.ok(dictionaryService.getLocationDictionary()).message("获取成功");
    }

    @GetMapping("/getLab")
    public Result getLabDictionary(){
        return Result.ok(dictionaryService.getLabDictionary()).message("获取成功");
    }

    @PostMapping("/merge")
    public Result mergeDictionary(@RequestParam String tagType, @RequestParam String tag, @RequestParam String targetTag){
        dictionaryService.mergeDictionary(new MergeDictionaryDTO(tagType, tag, targetTag));
        return Result.ok().message("合并成功");
    }
}
