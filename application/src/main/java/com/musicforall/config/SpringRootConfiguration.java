package com.musicforall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@ComponentScan({"com.musicforall.common"})
@Import({HibernateConfiguration.class,
        FileApiSpringConfig.class,
        SecurityConfig.class})
@PropertySource("classpath:/application.properties")
public class SpringRootConfiguration {

    @Autowired
    private Environment env;

    @Bean
    @Qualifier("files")
    public String filesDirectory() {
        return env.getRequiredProperty("files.directory");
    }
}
