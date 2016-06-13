package com.musicforall.config;

import com.musicforall.files.FileApiSpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@ComponentScan({"com.musicforall.common"})
@Import({HibernateConfiguration.class,
        FileApiSpringConfig.class})
@PropertySource("classpath:/application.properties")
public class SpringRootConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
                return new PropertySourcesPlaceholderConfigurer();
        }
}
