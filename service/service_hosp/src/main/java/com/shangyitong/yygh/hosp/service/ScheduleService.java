package com.shangyitong.yygh.hosp.service;


import com.shangyitong.yygh.model.hosp.Schedule;
import com.shangyitong.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> objectMap);

    Page<Schedule> findPageSchedule(int curPage, int curPageNum, ScheduleQueryVo scheduleQueryVo);

    void remove(String hosCode, String hosScheduleId);
}
