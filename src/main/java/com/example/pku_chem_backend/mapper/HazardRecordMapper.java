package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HazardRecordMapper extends BaseMapper<HazardRecordMapper> {
    @Insert("INSERT INTO hazard_record (id, type,location,request_date,approve_date, requester,processor,status) VALUES (#{id}, #{type}, #{location}, #{requestDate}, #{approveDate}, #{requester}, #{processor}, #{status})")
    void insertRecord(Integer id, String type, String location, String requestDate, String approveDate, String requester, String processor, String status);
}
