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
@ComponentScan({"com.musicforall.common"})
@Import({HibernateConfiguration.class,
        FileApiSpringConfig.class})
@PropertySource("classpath:/application.properties")
public class SpringRootConfiguration {
}
