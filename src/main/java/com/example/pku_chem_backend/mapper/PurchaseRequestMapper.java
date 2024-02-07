package com.example.pku_chem_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pku_chem_backend.entity.PurchaseRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface PurchaseRequestMapper extends BaseMapper<PurchaseRequest> {
    @Insert("INSERT INTO purchase_request (drug_id, source, buyer, request_date, quantity) VALUES (#{drug_id}, #{source}, #{buyer}, #{requestDate}, #{quantity})")
    void insertPurchaseRequest(Integer drug_id, String source, String buyer, String requestDate, Integer quantity);

    @Update("UPDATE purchase_request SET source = #{newSource} WHERE source = #{oldSource}")
    void replaceSource(String oldSource, String newSource);
}
