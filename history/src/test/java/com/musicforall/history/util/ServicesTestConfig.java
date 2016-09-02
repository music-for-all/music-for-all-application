package com.musicforall.history.util;

import com.musicforall.history.HibernateConfigDev;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Pukho on 19.06.2016.
 */
@Configuration
@ComponentScan(
        value = {"com.musicforall.history",
                "com.musicforall.common"})
@Import(HibernateConfigDev.class)
public class ServicesTestConfig {

}
