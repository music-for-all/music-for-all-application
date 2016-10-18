package com.musicforall.history;

import com.musicforall.common.cache.config.CacheConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by kgavrylchenko on 25.07.16.
 */
@Configuration
@ComponentScan("com.musicforall.history")
@Import({HibernateConfiguration.class,
        HibernateConfigDev.class,
})
@PropertySource(value = "file:${user.home}/application.properties")
public class HistorySpringConfig {
}
