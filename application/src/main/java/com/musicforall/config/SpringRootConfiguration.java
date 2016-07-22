package com.musicforall.config;

import com.musicforall.files.FileApiSpringConfig;
import org.springframework.context.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@ComponentScan({"com.musicforall.common",
        "com.musicforall.services, com.musicforall.history"})
@Import({HibernateConfiguration.class,
        HibernateConfigDev.class,
        FileApiSpringConfig.class,
        SecurityConfig.class})
@PropertySource(value = "file:${user.home}/application.properties")
public class SpringRootConfiguration {

    public static final int THREAD_POOL_SIZE = 10;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
}
