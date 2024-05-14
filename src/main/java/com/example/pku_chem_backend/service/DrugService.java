package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.dto.GetDrugDTO;
import com.example.pku_chem_backend.dto.GetDrugResultDTO;
import com.example.pku_chem_backend.dto.MultipleUpdateDrugDTO;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.mapper.DrugMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DrugService {
    @Autowired
    private DrugMapper drugMapper;

    public void createDrug(Drug drug){
        drugMapper.insert(drug);
    }

    public void deleteDrug(Integer id){
        drugMapper.deleteById(id);
    }

    public void updateDrug(Drug drug){
        drugMapper.updateById(drug);
    }

    public void multipleUpdateDrug(MultipleUpdateDrugDTO DTO){
        Integer[] ids = DTO.getIds();
        Class<?> objClass = DTO.getClass();
        while(objClass != null){
            Field[] fields = objClass.getDeclaredFields();
            for(Field field: fields){
                field.setAccessible(true);
                try {
                    Object filedValue = field.get(DTO);
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
                            drugMapper.updateField(fieldName, field.get(DTO).toString(), id);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            objClass = objClass.getSuperclass();
        }
    }

    /**
     * 更新药品的生产商
     * @param tag 要更新的生产商
     * @param targetTag 更新后的生产商
     */
    public void replaceProducer(String tag, String targetTag){
        drugMapper.replaceProducer(tag, targetTag);
    }

    /**
     * 更新药品的实验室
     * @param tag 要更新的实验室
     * @param targetTag 更新后的实验室
     */
    public void replaceLab(String tag, String targetTag){
        drugMapper.replaceLab(tag, targetTag);
    }

    /**
     * 更新药品的位置
     * @param tag 要更新的位置
     * @param targetTag 更新后的位置
     */
    public void replaceLocation(String tag, String targetTag){
        drugMapper.replaceLocation(tag, targetTag);
    }

    /**
     * 获取药品信息
     * @param DTO 获取药品的DTO
     * @param getAll 是否获取所有药品
     * @return 药品信息
     */
    public GetDrugResultDTO getDrug(GetDrugDTO DTO, boolean getAll){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        if(DTO.getId() != null){
            wrapper.eq("id", DTO.getId());
        }
        if(DTO.getName() != null){
            wrapper.like("name", DTO.getName()).or().like("nick_name", DTO.getName());
        }
        if(DTO.getProducer() != null){
            wrapper.like("producer", DTO.getProducer());
        }
        if(DTO.getFormula() != null){
            List<String> elements = splitFormula(DTO.getFormula());
            wrapper.like("formula", DTO.getFormula());
        }
        if(DTO.getCas() != null){
            wrapper.like("cas", DTO.getCas());
        }
        if(DTO.getLab() != null){
            wrapper.like("lab", DTO.getLab());
        }
        if(DTO.getLocation() != null){
            wrapper.like("location", DTO.getLocation());
        }
        if(DTO.getLayer() != null){
            wrapper.like("layer", DTO.getLayer());
        }
        if(DTO.getNote() != null){
            wrapper.like("note", DTO.getNote());
        }

        if(getAll){
            return GetDrugResultDTO.builder()
                    .total(Long.valueOf(drugMapper.selectCount(wrapper)))
                    .data(drugMapper.selectList(wrapper))
                    .build();
        } else {
            switch(DTO.getSort()){
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
            Page<Drug> pageParam = new Page<>(DTO.getPage(), DTO.getLimit());
            drugMapper.selectPage(pageParam, wrapper);
            return GetDrugResultDTO.builder()
                    .total(pageParam.getTotal())
                    .data(pageParam.getRecords())
                    .build();
        }
    }

    public Drug getDrugById(Integer id){
        return drugMapper.selectById(id);
    }

    /**
     * 将化学式分割为元素和数量
     * @param formula 化学式
     * @return 元素和数量的列表
     */
    private static List<String> splitFormula(String formula){
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



}
