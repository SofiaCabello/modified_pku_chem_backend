package com.example.pku_chem_backend.mapper;

import com.example.pku_chem_backend.entity.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {
    @Select("SELECT option FROM Dictionary WHERE type = #{type}")
    List<String> getOptionList(String type);
}
