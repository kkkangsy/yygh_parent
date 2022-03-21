package com.shangyitong.yygh.hosp.controller;

import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.hosp.service.HospitalService;
import com.shangyitong.yygh.model.hosp.Hospital;
import com.shangyitong.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin(allowCredentials = "true")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //医院列表
    @ApiOperation(value = "获取分页列表")
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,

            @ApiParam(name = "hospitalQueryVo", value = "查询对象", required = false)
                    HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospPageModel = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return Result.ok(hospPageModel);
    }

}

