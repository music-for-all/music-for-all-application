package com.musicforall.config;

import com.musicforall.files.FileApiSpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

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

    @Autowired
    private Environment env;

    @Bean
    @Qualifier("files")
    public String filesDirectory() {
        return env.getRequiredProperty("files.directory");
    }
}
