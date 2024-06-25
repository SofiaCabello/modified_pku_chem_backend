package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.dto.PurchaseRecordDTO;
import com.example.pku_chem_backend.entity.PurchaseRecord;
import com.example.pku_chem_backend.mapper.PurchaseRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseRecordService {
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private UserService userService;

    /**
     * 获取试剂采购记录
     * @param page 分页
     * @param limit 分页
     * @param drug_id 试剂id
     * @return 采购记录
     */
    public List<PurchaseRecordDTO> getRecord(Integer page, Integer limit, Integer drug_id) {
        Page<PurchaseRecord> pageParam = new Page<>(page, limit);
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("drug_id", drug_id);
        purchaseRecordMapper.selectPage(pageParam, wrapper);
        wrapper.orderByDesc("id");
        List<PurchaseRecord> list = pageParam.getRecords();
        return getPurchaseRecordDTOs(list);
    }

    public List<PurchaseRecordDTO> getRecentRecord() {
        // SELECT * FROM purchase_record WHERE approve_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND approve_date <= CURDATE()
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.apply("approve_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)");
        wrapper.orderByDesc("id");
        List<PurchaseRecord> list = purchaseRecordMapper.selectList(wrapper);
        System.out.println(list);
        return getPurchaseRecordDTOs(list);
//        List<PurchaseRecord> list = purchaseRecordMapper.getRecentRecord();
//        System.out.println(list);
//        return getPurchaseRecordDTOs(list);
    }

    public boolean addPurchaseRecord(PurchaseRecord purchaseRecord){
        return purchaseRecordMapper.insert(purchaseRecord) > 0;
    }

    private List<PurchaseRecordDTO> getPurchaseRecordDTOs(List<PurchaseRecord> list) {
        List<PurchaseRecordDTO> result = new ArrayList<>();
        for(PurchaseRecord record: list){
            PurchaseRecordDTO recordDTO = PurchaseRecordDTO.builder()
                    .purchaseRecord(record)
                    .buyerName(userService.getRealName(record.getBuyer()))
                    .processorName(userService.getRealName(record.getProcessor()))
                    .build();
            result.add(recordDTO);
        }
        return result;
    }
}
