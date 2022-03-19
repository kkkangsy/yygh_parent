package com.shangyitong.yygh.hosp.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shangyitong.yygh.hosp.mapper.HospitalSetMapper;
import com.shangyitong.yygh.hosp.service.HospitalSetService;
import com.shangyitong.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet>
        implements HospitalSetService {

    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper =new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(Objects.nonNull(hospitalSet)){
            return hospitalSet.getSignKey();
        }
        return null;
    }
}
