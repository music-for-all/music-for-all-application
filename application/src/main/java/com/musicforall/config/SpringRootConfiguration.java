package com.musicforall.config;

import com.musicforall.common.cache.GuavaCache;
import com.musicforall.common.cache.KeyValueRepository;
import com.musicforall.common.cache.config.CacheConfig;
import com.musicforall.config.security.SecurityConfig;
import com.musicforall.files.FileApiSpringConfig;
import com.musicforall.history.HistorySpringConfig;
import com.musicforall.model.Track;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static com.musicforall.common.cache.config.CacheConfig.GUAVA;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@EnableAsync
@ComponentScan({"com.musicforall.common",
        "com.musicforall.services"})
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

    @Bean(name = GUAVA)
    public KeyValueRepository<Integer, Track> cache() {
        return new GuavaCache<>();
    }
}
