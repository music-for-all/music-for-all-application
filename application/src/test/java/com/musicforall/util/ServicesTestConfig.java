package com.musicforall.util;

import com.musicforall.config.HibernateConfigDev;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.services.DbPopulateService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@ComponentScan(
        value = {"com.musicforall.services",
                "com.musicforall.common"},
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {DbPopulateService.class}))
@Import({HibernateConfigDev.class, SecurityConfig.class})
public class ServicesTestConfig {

}
