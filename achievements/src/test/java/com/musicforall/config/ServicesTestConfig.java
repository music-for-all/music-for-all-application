package com.musicforall.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Evgeniy on 17.10.2016.
 */
@Configuration
@ComponentScan(
        value = {"com.musicforall",
                "com.musicforall.common"})
@Import(HibernateConfigDev.class)
public class ServicesTestConfig {

}
