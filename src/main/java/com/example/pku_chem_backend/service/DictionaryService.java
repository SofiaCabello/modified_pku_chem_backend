package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pku_chem_backend.dto.AddDictionaryDTO;
import com.example.pku_chem_backend.dto.DeleteDictionaryDTO;
import com.example.pku_chem_backend.dto.GetDictionaryDTO;
import com.example.pku_chem_backend.dto.MergeDictionaryDTO;
import com.example.pku_chem_backend.entity.Dictionary;
import com.example.pku_chem_backend.mapper.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DictionaryService {
    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private DrugService drugService;
    @Autowired
    private HazardRequestService hazardRequestService;
    @Autowired
    private PurchaseRequestService purchaseRequestService;

    /**
     * 获取字典
     * @return 字典列表
     */
    public Map<String, List<String>> getDictionary(){
        List<String> tagTypes = Arrays.asList("producerTags", "labTags", "locationTags", "sourceTags", "wasteTags");
        List<GetDictionaryDTO> resultList = new ArrayList<>();
        Map<String, List<String>> result = new HashMap<>();

        for(String tagType : tagTypes){
            List<String> tagList = dictionaryMapper.getOptionsByType(tagType);
            result.put(tagType, tagList);
        }

        return result;
    }

    /**
     * 删除字典项
     * @param DTO 删除字典项的DTO
     * @return 是否删除成功
     */
    public boolean deleteDictionary(DeleteDictionaryDTO DTO){
        QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
        wrapper.eq("type", DTO.getTagType());
        wrapper.eq("options", DTO.getTag());
        return dictionaryMapper.delete(wrapper) > 0;
    }

    /**
     * 添加字典项
     * @param DTO 添加字典项的DTO
     * @return 是否添加成功
     */
    public boolean addDictionary(AddDictionaryDTO DTO){
        QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
        wrapper.eq("type", DTO.getTagType());
        wrapper.eq("options", DTO.getTag());
        if(dictionaryMapper.selectOne(wrapper) != null) {
            return false;
        } else {
            Dictionary dictionary = new Dictionary();
            dictionary.setType(DTO.getTagType());
            dictionary.setOptions(DTO.getTag());
            return dictionaryMapper.insert(dictionary) > 0;
        }
    }

    /**
     * 合并字典项
     * @param DTO 合并字典项的DTO
     */
    @Transactional
    public void mergeDictionary(MergeDictionaryDTO DTO){
        String tag = DTO.getTag();
        String targetTag = DTO.getTargetTag();
        switch(DTO.getTagType()) {
            case "producerTags":
                drugService.replaceProducer(tag, targetTag);
                break;
            case "labTags":
                drugService.replaceLab(tag, targetTag);
                hazardRequestService.replaceLab(tag, targetTag);
                break;
            case "locationTags":
                drugService.replaceLocation(tag, targetTag);
                hazardRequestService.replaceLocation(tag, targetTag);
                break;
            case "sourceTags":
                purchaseRequestService.replaceSource(tag, targetTag);
                break;
            case "wasteTags":
                hazardRequestService.replaceType(tag, targetTag);
                break;
        }
    }

    public List<String> getWasteDictionary(){
        return dictionaryMapper.getOptionsByType("wasteTags");
    }

    public List<String> getLocationDictionary(){
        return dictionaryMapper.getOptionsByType("locationTags");
    }

    public List<String> getLabDictionary(){
        return dictionaryMapper.getOptionsByType("labTags");
    }
}
