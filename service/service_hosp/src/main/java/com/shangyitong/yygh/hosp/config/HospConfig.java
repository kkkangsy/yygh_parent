package com.shangyitong.yygh.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.shangyitong.yygh.hosp.mapper")
public class HospConfig {
}
