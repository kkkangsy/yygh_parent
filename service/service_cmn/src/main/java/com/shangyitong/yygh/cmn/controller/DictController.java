package com.shangyitong.yygh.cmn.controller;

import com.shangyitong.yygh.cmn.mapper.DictMapper;
import com.shangyitong.yygh.cmn.service.DictService;
import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin(allowCredentials = "true")
public class DictController {
    @Autowired
    private DictService dictService;

    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable("id") Long id){
       List<Dict> list = dictService.findChildData(id);
       return Result.ok(list);
    }

    //导出数据字典
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }

    //导入数据字典
    @PostMapping("importData")
    public Result importDict(MultipartFile file) throws IOException {
        dictService.importDictData(file);
        return Result.ok();
    }
}
