package com.shangyitong.yygh.cmn.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shangyitong.yygh.cmn.Listener.DictListener;
import com.shangyitong.yygh.cmn.mapper.DictMapper;
import com.shangyitong.yygh.cmn.service.DictService;
import com.shangyitong.yygh.common.result.Result;
import com.shangyitong.yygh.model.cmn.Dict;
import com.shangyitong.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
        implements DictService {

    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    @Override
    public  List<Dict> findChildData(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //向list中的Dict设置hasChildren
        dictList.stream().forEach(dict->{
            Long dictId =dict.getId();
            boolean children = this.isChildren(dictId);
            dict.setHasChildren(children);
        });
        return dictList;
    }

    //判断id下是否有子数据
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0;
    }


    //导出字典信息
    @Override
    public void exportDictData(HttpServletResponse response){
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            //String fileName = "dict";
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            //查询数据库
            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for(Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictVo);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @CacheEvict(value = "dict", allEntries=true)
    @Override
    public Result importDictData(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        return null;
    }

    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {
        if(StringUtils.isEmpty(parentDictCode)){
            QueryWrapper<Dict> wrapper =new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else{
            Dict parentDict = this.getDictByDictCode(parentDictCode);
            Long parentId = parentDict.getId();

            //根据parentId与value共同查询
            QueryWrapper<Dict> wrapper =new QueryWrapper<>();
            wrapper.eq("parent_id",parentId);
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict parentDict = this.getDictByDictCode(dictCode);
        if(null == parentDict) return null;
        //根据id获取子节点
        List<Dict> dictList = this.findChildData(parentDict.getId());
        return dictList;
    }

    private Dict getDictByDictCode(String parentDictCode){
        QueryWrapper<Dict> wrapper =new QueryWrapper<>();
        wrapper.eq("dict_code",parentDictCode);
        Dict parentDict = baseMapper.selectOne(wrapper);
        return parentDict;
    }

}
