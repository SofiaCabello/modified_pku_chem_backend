package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.Drug;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.GetMapping;

@Mapper
public interface DrugMapper extends BaseMapper<Drug> {
    @Insert("insert into drug(name, producer, specification, nick_name, formula, cas, lab, location, layer, url, stock, note) values(#{name}, #{producer}, #{specification}, #{nickName}, #{formula}, #{cas}, #{lab}, #{location}, #{layer}, #{url}, #{stock}, #{note})")
    void insertDrug(String name, String producer, String specification, String nickName, String formula, String cas, String lab, String location, Integer layer, String url, Integer stock, String note);

    @Update("UPDATE drug SET producer = #{newProducer} WHERE producer = #{oldProducer}")
    void replaceProducer(String oldProducer, String newProducer);

    @Update("UPDATE drug SET location = REPLACE(location,  #{oldLocation}, #{newLocation})")
    void replaceLocation(String oldLocation, String newLocation);

    @Update("UPDATE drug SET lab = #{targetTag} WHERE lab = #{tag}")
    void replaceLab(String tag, String targetTag);

    @Update("UPDATE drug SET ${filed} = #{value} WHERE id = #{id}")
    void updateField(String filed, String value, Integer id);
}
