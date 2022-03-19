package com.shangyitong.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shangyitong.yygh.hosp.repository.HospitalRepository;
import com.shangyitong.yygh.hosp.service.HospitalService;
import com.shangyitong.yygh.model.hosp.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> objectMap){
        log.info(JSONObject.toJSONString(objectMap));
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(objectMap), Hospital.class);
        //判断是否存在相同数据；
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());

        //如存在，则更新
        if(null != targetHospital) {
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            //，如不存在，进行添加
            hospital.setStatus(0);  //0：未上线 1：已上线
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHospCode(String hosCode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hosCode);
        return hospital;
    }
}
