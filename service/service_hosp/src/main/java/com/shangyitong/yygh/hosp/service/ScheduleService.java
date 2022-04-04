package com.shangyitong.yygh.hosp.service;


import com.shangyitong.yygh.model.hosp.Schedule;
import com.shangyitong.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> objectMap);

    Page<Schedule> findPageSchedule(int curPage, int curPageNum, ScheduleQueryVo scheduleQueryVo);

    void remove(String hosCode, String hosScheduleId);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
