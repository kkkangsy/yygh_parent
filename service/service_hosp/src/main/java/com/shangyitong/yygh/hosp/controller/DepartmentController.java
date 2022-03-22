package com.shangyitong.yygh.hosp.controller;


import com.shangyitong.yygh.hosp.service.DepartmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "科室管理接口")
@RestController
@RequestMapping("/admin/hosp/department")
@CrossOrigin(allowCredentials = "true")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    //根据医院编号，查询医院所有科室

}
