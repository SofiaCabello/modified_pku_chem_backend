package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.Drug;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.springframework.web.bind.annotation.GetMapping;

@Mapper
public interface DrugMapper extends BaseMapper<Drug> {
}
