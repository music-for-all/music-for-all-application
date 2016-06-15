package com.musicforall.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@ComponentScan({"com.musicforall.common"})
@Import({HibernateConfiguration.class, SecurityConfig.class})
public class SpringRootConfiguration {
}
