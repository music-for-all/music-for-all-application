package com.musicforall.config;

import com.google.common.cache.CacheBuilder;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.files.FileApiSpringConfig;
import com.musicforall.history.HistorySpringConfig;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@EnableAsync
@EnableCaching
@ComponentScan({"com.musicforall.common",
        "com.musicforall.services",
        "com.musicforall.persistence"})
@Import({HibernateConfiguration.class,
        HibernateConfigDev.class,
        FileApiSpringConfig.class,
        HistorySpringConfig.class,
        LocaleConfig.class,
        SecurityConfig.class,
        MailConfig.class
})
@PropertySource(value = "file:${user.home}/application.properties")
public class SpringRootConfiguration implements AsyncConfigurer {

    public static final int THREAD_POOL_SIZE = 10;

    @Bean
    public ExecutorService executorService() {
        return newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Override
    public Executor getAsyncExecutor() {
        return executorService();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Bean
    public CacheManager cacheManager() {
        final long maxSize = 100;
        final long expirePeriod = 10;

        final GuavaCacheManager cacheManager = new GuavaCacheManager("guava");
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirePeriod, TimeUnit.MINUTES);

        cacheManager.setCacheBuilder(cacheBuilder);
        return cacheManager;
    }
}
