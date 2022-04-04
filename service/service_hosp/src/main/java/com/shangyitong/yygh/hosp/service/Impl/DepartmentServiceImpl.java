package com.shangyitong.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shangyitong.yygh.hosp.repository.DepartmentRepository;
import com.shangyitong.yygh.hosp.service.DepartmentService;
import com.shangyitong.yygh.model.hosp.Department;
import com.shangyitong.yygh.vo.hosp.DepartmentQueryVo;
import com.shangyitong.yygh.vo.hosp.DepartmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> list =new ArrayList<>();

        //查询医院的所有科室信息
        Department departmentQuery =new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        List<Department> departmentList = departmentRepository.findAll(example);

        //根据大科室编号，获取每个大科室下面的子级科室<大科室编号，大科室下的所有子级科室>
        Map<String, List<Department>> departmentListMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

        for (Map.Entry<String, List<Department>> entry: departmentListMap.entrySet()) {
            String bigCode = entry.getKey();
            List<Department> subDepList = entry.getValue();
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(subDepList.get(0).getBigname());
            List<DepartmentVo> childList =new ArrayList<>();

            for(Department subDep:subDepList){
                DepartmentVo subDepartmentVo = new DepartmentVo();
                subDepartmentVo.setDepcode(subDep.getDepcode());
                subDepartmentVo.setDepname(subDep.getDepname());
                childList.add(subDepartmentVo);
            }
            departmentVo.setChildren(childList);
            list.add(departmentVo);

        }
        return list;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        if(Objects.nonNull(targetDepartment)){
            return targetDepartment.getDepname();
        }
        return null;
    }


}
