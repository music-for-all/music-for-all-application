package com.musicforall.config;

import com.musicforall.cache.NotificationCacheProvider;
import com.musicforall.cache.StreamCacheProvider;
import com.musicforall.common.cache.CacheProvider;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.files.FileApiSpringConfig;
import com.musicforall.history.HistorySpringConfig;
import com.musicforall.model.Track;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.musicforall.config.CacheConfig.NOTIFICATION;
import static com.musicforall.config.CacheConfig.STREAM;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan({"com.musicforall.common",
        "com.musicforall.services",
        "com.musicforall.notifications"})
@Import({HibernateConfiguration.class,
        HibernateConfigDev.class,
        FileApiSpringConfig.class,
        HistorySpringConfig.class,
        LocaleConfig.class,
        SecurityConfig.class,
        MailConfig.class,
        CacheConfig.class
})
@PropertySource(value = "file:${user.home}/application.properties")
public class SpringRootConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    public static final int THREAD_POOL_SIZE = 10;

    @Bean
    public ExecutorService executorService() {
        return newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Bean
    public Executor taskScheduler() {
        return newScheduledThreadPool(THREAD_POOL_SIZE);
    }

    @Bean(name = NOTIFICATION)
    public CacheProvider<Integer, AtomicInteger> cacheNews() {
        return new NotificationCacheProvider();
    }

    @Bean(name = STREAM)
    public CacheProvider<Integer, Track> cacheStream() {
        return new StreamCacheProvider();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Override
    public Executor getAsyncExecutor() {
        return executorService();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }
}
