package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseRecordMapper extends BaseMapper<PurchaseRecord> {
    @Insert("INSERT INTO purchase_record (id, drug_id, buyer, source, processor, approve_date, request_date) VALUES (#{id}, #{drugId}, #{buyer}, #{source}, #{processor}, #{approveDate}, #{requestDate})")
    void insertRecord(Integer id, Integer drugId, String buyer, String source, String processor, String approveDate, String requestDate);
}
