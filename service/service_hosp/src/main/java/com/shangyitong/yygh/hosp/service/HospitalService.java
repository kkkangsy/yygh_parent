package com.shangyitong.yygh.hosp.service;

import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> objectMap);

    Hospital getByHospCode(String hosCode);

}
