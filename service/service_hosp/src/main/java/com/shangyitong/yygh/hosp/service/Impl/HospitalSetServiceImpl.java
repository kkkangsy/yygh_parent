package com.shangyitong.yygh.hosp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shangyitong.yygh.hosp.mapper.HospitalSetMapper;
import com.shangyitong.yygh.hosp.service.HospitalSetService;
import com.shangyitong.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet>
        implements HospitalSetService {
}
