package com.shangyitong.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shangyitong.yygh.hosp.repository.ScheduleRepository;
import com.shangyitong.yygh.hosp.service.ScheduleService;
import com.shangyitong.yygh.model.hosp.Schedule;
import com.shangyitong.yygh.vo.hosp.ScheduleQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private  ScheduleRepository scheduleRepository;

    @Override
    public void save(Map<String, Object> objectMap) {
        log.info(JSONObject.toJSONString(objectMap));
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(objectMap), Schedule.class);

        //判断是否存在相同数据；
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(
                schedule.getHoscode(), schedule.getHosScheduleId());

        //如存在，则更新
        if(null != targetSchedule) {
            schedule.setCreateTime(targetSchedule.getCreateTime());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        } else {
            //，如不存在，进行添加
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPageSchedule(int curPage, int curPageNum, ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable = PageRequest.of(curPage-1,curPageNum);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Schedule schedule =new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);

        Example<Schedule> example =Example.of(schedule,matcher);
        Page<Schedule> schedules = scheduleRepository.findAll(example, pageable);
        return schedules;
    }

    @Override
    public void remove(String hosCode, String hosScheduleId) {
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hosCode,hosScheduleId);

        if(null != targetSchedule){
            scheduleRepository.deleteById(targetSchedule.getId());
        }
    }
}
