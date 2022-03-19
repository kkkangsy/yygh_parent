package com.shangyitong.yygh.hosp.service;

import com.shangyitong.yygh.model.hosp.Department;
import com.shangyitong.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {

    void save(Map<String, Object> objectMap);
    

    Page<Department> findPageDepartment(int curPage, int curPageNum, DepartmentQueryVo departmentQueryVo);

    void remove(String hosCode, String depCode);
}
