package com.shangyitong.yygh.hosp.service;

import com.shangyitong.yygh.model.hosp.Department;
import com.shangyitong.yygh.vo.hosp.DepartmentQueryVo;
import com.shangyitong.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {

    void save(Map<String, Object> objectMap);
    

    Page<Department> findPageDepartment(int curPage, int curPageNum, DepartmentQueryVo departmentQueryVo);

    void remove(String hosCode, String depCode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);
}
