package com.musicforall.util;

import com.musicforall.common.cache.config.CacheConfig;
import com.musicforall.config.HibernateConfigDev;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.history.HistorySpringConfig;
import com.musicforall.services.DbPopulateService;
import com.musicforall.services.file.FileServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@EnableCaching
@ComponentScan(
        value = {"com.musicforall.services",
                "com.musicforall.common",
                "com.musicforall.cache"},
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value =
                {DbPopulateService.class, FileServiceImpl.class}))
@PropertySource(value = "classpath:application.properties")
@Import({HibernateConfigDev.class, SecurityConfig.class,
        HistorySpringConfig.class, TestMessageConfig.class,
        CacheConfig.class})
public class ServicesTestConfig {
}
