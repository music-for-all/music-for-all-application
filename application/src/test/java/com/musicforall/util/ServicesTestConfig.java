package com.musicforall.util;

import com.musicforall.cache.NotificationCacheProvider;
import com.musicforall.cache.StreamCacheProvider;
import com.musicforall.common.cache.CacheProvider;
import com.musicforall.config.CacheConfig;
import com.musicforall.config.HibernateConfigDev;
import com.musicforall.config.WebSocketConfig;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.history.HistorySpringConfig;
import com.musicforall.model.Track;
import com.musicforall.notifications.Notification;
import com.musicforall.services.DbPopulateService;
import com.musicforall.services.file.FileServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

import java.util.Collection;

import static com.musicforall.config.CacheConfig.NOTIFICATION;
import static com.musicforall.config.CacheConfig.STREAM;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@EnableCaching
@ComponentScan(
        value = {"com.musicforall.services",
                "com.musicforall.common",
                "com.musicforall.notifications",
                "com.musicforall.web.stream"},
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value =
                {DbPopulateService.class, FileServiceImpl.class}))
@PropertySource(value = "classpath:application.properties")
@Import({HibernateConfigDev.class, SecurityConfig.class, TestMessageConfig.class,
        HistorySpringConfig.class, CacheConfig.class, WebSocketConfig.class})
public class ServicesTestConfig {

    @Bean(name = NOTIFICATION)
    public CacheProvider<Integer, Collection<Notification>> cacheNews() {
        return new NotificationCacheProvider();
    }

    @Bean(name = STREAM)
    public CacheProvider<Integer, Track> cacheStream() {
        return new StreamCacheProvider();
    }
}
