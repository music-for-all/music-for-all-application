package com.musicforall.config;

import com.musicforall.files.FileApiSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@ComponentScan({"com.musicforall.common",
        "com.musicforall.services"})
@Import({HibernateConfiguration.class,
        HibernateConfigDev.class,
        FileApiSpringConfig.class,
        SecurityConfig.class})
@PropertySource(value = "file:${user.home}/application.properties")
public class SpringRootConfiguration {
}
