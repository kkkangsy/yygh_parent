package com.shangyitong.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shangyitong.yygh.cmn.client.DictFeignClient;
import com.shangyitong.yygh.enums.DictEnum;
import com.shangyitong.yygh.hosp.repository.HospitalRepository;
import com.shangyitong.yygh.hosp.service.HospitalService;
import com.shangyitong.yygh.model.hosp.Hospital;
import com.shangyitong.yygh.vo.hosp.HospitalQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

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

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page-1,limit);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital =new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        hospital.setIsDeleted(0);

        Example<Hospital> example =Example.of(hospital,matcher);
        Page<Hospital> hospitals = hospitalRepository.findAll(example, pageable);

        //获取查询list集合，遍历进行医院等级封装
        hospitals.getContent().stream().forEach(hosp->{
           this.setHospHosTypeAndpLocation(hosp);
        });

        return hospitals;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object>  getHospDetailById(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = hospitalRepository.findById(id).get();
        this.setHospHosTypeAndpLocation(hospital);
        result.put("hospital", hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    private Hospital setHospHosTypeAndpLocation(Hospital hosp) {
        String hosType = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(),hosp.getHostype());
        String hosProvinceCode = dictFeignClient.getName(hosp.getProvinceCode());
        String hosCityCode = dictFeignClient.getName(hosp.getCityCode());
        String hosDistrictCode = dictFeignClient.getName(hosp.getDistrictCode());
        hosp.getParam().put("fullAddress",hosProvinceCode+hosCityCode+hosDistrictCode);
        hosp.getParam().put("hosType",hosType);
        return hosp;
    }


}
