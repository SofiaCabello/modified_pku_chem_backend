package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.HazardRecord;
import com.example.pku_chem_backend.entity.HazardRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HazardRecordMapper extends BaseMapper<HazardRecordMapper> {
    @Insert("INSERT INTO hazard_record (id, type,location,request_date,approve_date, requester,processor) VALUES (#{id}, #{type}, #{location}, #{requestDate}, #{approveDate}, #{requester}, #{processor})")
    void insertRecord(Integer id, String type, String location, String requestDate, String approveDate, String requester, String processor);
    @Select("SELECT * FROM hazard_record WHERE approve_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND approve_date <= CURDATE()")
    List<HazardRecord> getRecentRecord();
}
