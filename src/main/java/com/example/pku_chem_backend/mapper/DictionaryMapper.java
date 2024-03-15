package com.example.pku_chem_backend.mapper;

import com.example.pku_chem_backend.entity.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    @Select("select type, options from dictionary order by type desc")
    List<Map<String, String>> getDictionaryDesc(); // 获取字典表，type降序

    @Select("select options from dictionary where type = #{type} order by CONVERT(options USING gbk) asc")
    List<String> getOptionsByType(String type); // 根据type获取options

    @Insert("insert into dictionary (type, options) values (#{type}, #{options})")
    void insertDictionary(String type, String options); // 插入字典表
}
