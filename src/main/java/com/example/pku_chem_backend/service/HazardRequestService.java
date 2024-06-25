package com.example.pku_chem_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pku_chem_backend.dto.GetRequestDTO;
import com.example.pku_chem_backend.dto.RecentHazardRecordDTO;
import com.example.pku_chem_backend.entity.HazardRecord;
import com.example.pku_chem_backend.entity.HazardRequest;
import com.example.pku_chem_backend.mapper.HazardRecordMapper;
import com.example.pku_chem_backend.mapper.HazardRequestMapper;
import com.example.pku_chem_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HazardRequestService {
    @Autowired
    private HazardRequestMapper hazardRequestMapper;
    @Autowired
    private HazardRecordMapper hazardRecordMapper;
    @Autowired
    private UserService userService;

    public void replaceLab(String tag, String targetTag){
        hazardRequestMapper.replaceLab(tag, targetTag);
    }

    public void replaceLocation(String tag, String targetTag){
        hazardRequestMapper.replaceLocation(tag, targetTag);
    }

    public void replaceType(String tag, String targetTag) {
        hazardRequestMapper.replaceType(tag, targetTag);
    }

    public List<HazardRequest> getRequest(GetRequestDTO DTO, String token){
        Page<HazardRequest> pageParam = new Page<>(DTO.getPage(), DTO.getLimit());
        QueryWrapper<HazardRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        String username = JwtUtil.getUsername(token);
        wrapper.eq("requester", username); // 只能看到自己的申请
        hazardRequestMapper.selectPage(pageParam, wrapper);
        return pageParam.getRecords();
    }

    public boolean createRequest(HazardRequest hazardRequest){
        hazardRequest.setStatus("pending");
        return hazardRequestMapper.insert(hazardRequest) == 1;
    }

    public List<HazardRequest> getAllRequest(Integer page, Integer limit) {
        Page<HazardRequest> pageParam = new Page<>(page, limit);
        QueryWrapper<HazardRequest> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id").eq("status", "pending");
        hazardRequestMapper.selectPage(pageParam, wrapper);
        return pageParam.getRecords();
    }

    public boolean processRequest(Integer id, boolean approve) {
        HazardRequest hazardRequest = hazardRequestMapper.selectById(id);
        if (hazardRequest == null) {
            return false;
        }
        if (approve) {
            hazardRequest.setStatus("approved");
        } else {
            hazardRequest.setStatus("rejected");
        }
        return hazardRequestMapper.updateById(hazardRequest) == 1;
    }

    public boolean deleteRequest(Integer id) {
        return hazardRequestMapper.deleteById(id) == 1;
    }

    public List<RecentHazardRecordDTO> getRecentRecord(){
        List<HazardRecord> records = hazardRecordMapper.getRecentRecord();
        List<RecentHazardRecordDTO> result = new ArrayList<>();
        for(HazardRecord record: records){
            RecentHazardRecordDTO dto = RecentHazardRecordDTO.builder()
                    .hazardRecord(record)
                    .requesterName(userService.getRealName(record.getRequester()))
                    .processorName(userService.getRealName(record.getProcessor()))
                    .build();
            result.add(dto);
        }
        return result;
    }

}
