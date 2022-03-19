package com.shangyitong.yygh.hosp.controller.api;


import com.shangyitong.yygh.common.exception.YyghException;
import com.shangyitong.yygh.common.helper.HttpRequestHelper;
import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.common.result.ResultCodeEnum;
import com.shangyitong.yygh.common.utils.MD5;
import com.shangyitong.yygh.hosp.service.DepartmentService;
import com.shangyitong.yygh.hosp.service.HospitalService;
import com.shangyitong.yygh.hosp.service.HospitalSetService;
import com.shangyitong.yygh.hosp.service.ScheduleService;
import com.shangyitong.yygh.model.hosp.Department;
import com.shangyitong.yygh.model.hosp.Hospital;
import com.shangyitong.yygh.model.hosp.Schedule;
import com.shangyitong.yygh.vo.hosp.DepartmentQueryVo;
import com.shangyitong.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
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

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        if(StringUtils.isEmpty(hosCode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)objectMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            objectMap.put("logoData", logoData);
        }


        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        hospitalService.save(objectMap);
        return Result.ok();
    }

    //查询医院接口
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        if(StringUtils.isEmpty(hosCode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用方法查询医院数据
        Hospital hospital = hospitalService.getByHospCode(hosCode);
        return Result.ok(hospital);
    }



    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        if(StringUtils.isEmpty(hosCode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(objectMap);
        return Result.ok();
    }

    //查询医院接口
    @PostMapping("department/list")
    public Result getDepartment(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        String page = (String)objectMap.get("page");
        int curPage=0,curPageNum=0;
        if(StringUtils.isEmpty(page)) {
            curPage =1;
        }else{
            curPage = Integer.parseInt(page);
        }

        String pageNum = (String)objectMap.get("limit");
        if(StringUtils.isEmpty(pageNum)) {
            curPageNum =1;
        }else{
            curPageNum = Integer.parseInt(pageNum);
        }

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo =new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hosCode);

        //调用方法查询医院数据
        Page<Department> departmentPage = departmentService.findPageDepartment(curPage,curPageNum,departmentQueryVo);
        return Result.ok(departmentPage);
    }

    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        String depCode = (String)objectMap.get("depcode");

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hosCode,depCode);
        return  Result.ok();
    }

    //上传排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        if(StringUtils.isEmpty(hosCode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(objectMap);
        return Result.ok();
    }

    //查询医院接口
    @PostMapping("schedule/list")
    public Result getSchedule(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        String depCode = (String)objectMap.get("depcode");

        String page = (String)objectMap.get("page");
        int curPage=0,curPageNum=0;
        if(StringUtils.isEmpty(page)) {
            curPage =1;
        }else{
            curPage = Integer.parseInt(page);
        }

        String pageNum = (String)objectMap.get("limit");
        if(StringUtils.isEmpty(pageNum)) {
            curPageNum =1;
        }else{
            curPageNum = Integer.parseInt(pageNum);
        }

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo =new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hosCode);
        scheduleQueryVo.setHoscode(depCode);

        //调用方法查询医院数据
        Page<Schedule> departmentPage = scheduleService.findPageSchedule(curPage,curPageNum,scheduleQueryVo);
        return Result.ok(departmentPage);
    }

    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        //得到医院系统（hospital-manage）传过来的医院数据
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestParameterMap);

        //获取医院编号
        //必须参数校验
        String hosCode = (String)objectMap.get("hoscode");
        String hosScheduleId = (String)objectMap.get("hosScheduleId");

        //签名校验
        //获取医院系统传递过来的进行MD5加密后的签名
        String hospSign =(String) objectMap.get("sign");

        //根据传递过来的医院编码，查询数据库（yygh_hosp.hospital_set），查询签名
        String hospSetSign = hospitalSetService.getSignKey(hosCode);

        //把数据库中查出来的签名也进行MD5加密
        String signKeyMd5= MD5.encrypt(hospSetSign);
        if(!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.remove(hosCode,hosScheduleId);
        return  Result.ok();
    }

}
