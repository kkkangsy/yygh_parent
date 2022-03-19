package com.shangyitong.yygh.hosp.controller.api;

import com.shangyitong.yygh.common.helper.HttpRequestHelper;
import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.hosp.service.HospitalService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);
        hospitalService.save(objectMap);
        return Result.ok();
    }

}
