package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.HazardRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HazardRequestMapper extends BaseMapper<HazardRequest> {
    @Insert("INSERT INTO hazard_request (requester, request_date, type, location, status) VALUES (#{requester}, #{requestDate}, #{type}, #{location}, 'pending')")
    void insertHazardRequest(String requester, String requestDate, String type, String location);
    @Update("UPDATE hazard_request SET type = #{newType} WHERE type = #{oldType}")
    void replaceType(String oldType, String newType);
    @Update("UPDATE hazard_request SET location = REPLACE(location,  #{oldLocation}, #{newLocation})")
    void replaceLocation(String oldLocation, String newLocation);
    @Update("UPDATE hazard_request SET location = REPLACE(location, #{oldLab}, #{newLab})")
    void replaceLab(String oldLab, String newLab);
    @Update("UPDATE hazard_request SET status = #{status} WHERE id = #{id}")
    void updateStatus(Integer id, String status);
}
