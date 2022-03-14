package com.shangyitong.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response);

    Result importDictData(MultipartFile file) throws IOException;
}


