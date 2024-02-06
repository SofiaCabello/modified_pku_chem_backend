package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.HazardRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HazardRequestMapper extends BaseMapper<HazardRequest> {
    @Insert("INSERT INTO hazard_request (requester, request_date, type, location) VALUES (#{requester}, #{requestDate}, #{type}, #{location})")
    void insertHazardRequest(String requester, String requestDate, String type, String location);

}
