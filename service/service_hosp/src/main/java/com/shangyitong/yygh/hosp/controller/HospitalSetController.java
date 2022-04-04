package com.shangyitong.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.common.utils.MD5;
import com.shangyitong.yygh.hosp.service.HospitalSetService;
import com.shangyitong.yygh.model.hosp.HospitalSet;
import com.shangyitong.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2 逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if(flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //3 条件查询，带分页
    @ApiOperation(value = "带分页条件查询医院设置")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable("current") long current, @PathVariable("limit") long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        //创建page对象
        Page<HospitalSet> page  = new Page<>(current,limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper =new QueryWrapper<>();
        //医院名称
        String hosname = hospitalSetQueryVo.getHosname();
        //医院编号
        String hoscode = hospitalSetQueryVo.getHoscode();

        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname", hosname);
        }

        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode", hoscode);
        }

        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        //返回结果
        return Result.ok(pageHospitalSet);
    }


    //添加医院设置
    @ApiOperation(value = "添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        hospitalSet.setStatus(1);

        Random random = new Random();
        String encrypt = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(encrypt);

        boolean flag = hospitalSetService.save(hospitalSet);
        if(flag){
            return  Result.ok();
        }else{
            return Result.fail();
        }
    }

    //根据id获取医院设置
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable("id") long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //修改医院设置
    @ApiOperation(value = "修改医院设置")
    @PostMapping("updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return  Result.ok();
        }else{
            return Result.fail();
        }
    }

    //批量删除医院设置
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchRemoveHospitalSet")
    public Result batchRemoveHospitalSet(@RequestBody List<String> idList){
        hospitalSetService.removeByIds(idList);
        return  Result.ok();
    }

    //医院设置锁定和解锁
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHosspitalSet(@PathVariable("id") long id, @PathVariable("status") Integer status){
        //根据id查询医院设置信息
        HospitalSet hosp = hospitalSetService.getById(id);
        //设置状态
        hosp.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hosp);
        return Result.ok();
    }

    //发送签名密钥
    @PutMapping("sendKey/{id}")
    public Result lockHosspitalSet(@PathVariable("id") long id){
        //根据id查询医院设置信息
        HospitalSet hosp = hospitalSetService.getById(id);
        String signKey = hosp.getSignKey();
        String hoscode = hosp.getHoscode();
        //TODO sendMessage
        return Result.ok();
    }

}
