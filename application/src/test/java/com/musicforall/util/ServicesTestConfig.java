package com.musicforall.util;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@ComponentScan({"com.musicforall.services",
        "com.musicforall.common"})
@Import({JpaServicesTestConfig.class})
public class ServicesTestConfig {
}
