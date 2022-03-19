package com.shangyitong.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shangyitong.yygh.hosp.repository.DepartmentRepository;
import com.shangyitong.yygh.hosp.service.DepartmentService;
import com.shangyitong.yygh.model.hosp.Department;
import com.shangyitong.yygh.vo.hosp.DepartmentQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> objectMap) {
        log.info(JSONObject.toJSONString(objectMap));
        Department department = JSONObject.parseObject(JSONObject.toJSONString(objectMap), Department.class);

        //判断是否存在相同数据；
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(
                department.getHoscode(), department.getDepcode());

        //如存在，则更新
        if(null != targetDepartment) {
            department.setCreateTime(targetDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        } else {
            //，如不存在，进行添加
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(int curPage, int curPageNum, DepartmentQueryVo departmentQueryVo) {
        Pageable pageable = PageRequest.of(curPage-1,curPageNum);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Department department =new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        Example<Department> example =Example.of(department,matcher);
        Page<Department> departments = departmentRepository.findAll(example, pageable);
        return departments;
    }

    @Override
    public void remove(String hosCode, String depCode) {
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(hosCode,depCode);

        if(null != targetDepartment){
            departmentRepository.deleteById(targetDepartment.getId());
        }
    }


}
