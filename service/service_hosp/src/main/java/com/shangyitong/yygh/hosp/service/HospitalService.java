package com.shangyitong.yygh.hosp.service;

import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.model.hosp.Hospital;
import com.shangyitong.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> objectMap);

    Hospital getByHospCode(String hosCode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object>  getHospDetailById(String id);
}
