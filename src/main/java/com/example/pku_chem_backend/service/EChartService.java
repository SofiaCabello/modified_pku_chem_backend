package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pku_chem_backend.dto.ProducerRatioDTO;
import com.example.pku_chem_backend.entity.Drug;
import com.example.pku_chem_backend.mapper.DictionaryMapper;
import com.example.pku_chem_backend.mapper.DrugMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EChartService {
    @Autowired
    private DrugMapper drugMapper;
    @Autowired
    private DictionaryMapper dictionaryMapper;

    public List<ProducerRatioDTO> getProducerCountMap(){
        List<String> producerList = dictionaryMapper.getOptionsByType("producerTags");
        List<ProducerRatioDTO> producerRatioDTOList = new ArrayList<>();
        for(String producer: producerList) {
            QueryWrapper<Drug> wrapper = new QueryWrapper();
            wrapper.eq("producer", producer);
            int count = drugMapper.selectCount(wrapper);
            System.out.println(producer + " " + count);
            ProducerRatioDTO producerRatioDTO = ProducerRatioDTO.builder()
                    .name(producer)
                    .value(count)
                    .build();
            producerRatioDTOList.add(producerRatioDTO);
        }
        return producerRatioDTOList;
    }

    private int getProducerCount(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", "producerTags");
        int producerCount = dictionaryMapper.selectCount(wrapper);
        return producerCount;
    }
}
