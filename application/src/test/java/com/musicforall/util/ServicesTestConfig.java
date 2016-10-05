package com.musicforall.util;

import com.musicforall.common.cache.GuavaCache;
import com.musicforall.common.cache.KeyValueRepository;
import com.musicforall.common.cache.config.CacheConfig;
import com.musicforall.config.HibernateConfigDev;
import com.musicforall.config.WebSocketConfig;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.history.HistorySpringConfig;
import com.musicforall.model.Track;
import com.musicforall.services.DbPopulateService;
import com.musicforall.services.file.FileServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

import static com.musicforall.common.cache.config.CacheConfig.GUAVA;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@EnableCaching
@ComponentScan(
        value = {"com.musicforall.services",
                "com.musicforall.common",
                "com.musicforall.web.stream"},
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value =
                {DbPopulateService.class, FileServiceImpl.class}))
@PropertySource(value = "classpath:application.properties")
@Import({HibernateConfigDev.class, SecurityConfig.class, TestMessageConfig.class,
        HistorySpringConfig.class, CacheConfig.class, WebSocketConfig.class})
public class ServicesTestConfig {
    @Bean(name = GUAVA)
    public KeyValueRepository<Integer, Track> cache() {
        return new GuavaCache<>();
    }
}
