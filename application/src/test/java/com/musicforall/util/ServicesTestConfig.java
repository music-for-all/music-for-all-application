package com.musicforall.util;

import com.musicforall.config.HibernateConfigDev;
import com.musicforall.config.MessageConfigDev;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.services.DbPopulateService;
import org.springframework.context.annotation.*;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@ComponentScan(
        value = {"com.musicforall.services",
                "com.musicforall.common"},
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {DbPopulateService.class}))
@PropertySource(value = "classpath:application.properties")
@Import({HibernateConfigDev.class, SecurityConfig.class, MessageConfigDev.class})
public class ServicesTestConfig {

}
