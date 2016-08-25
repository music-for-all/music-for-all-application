package com.musicforall.config;

import com.musicforall.config.security.SecurityConfig;
import com.musicforall.files.FileApiSpringConfig;
import com.musicforall.history.HistorySpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kgavrylchenko on 10.06.16.
 */
@Configuration
@ComponentScan({"com.musicforall.common",
        "com.musicforall.services"})
@Import({HibernateConfiguration.class,
        HibernateConfigDev.class,
        FileApiSpringConfig.class,
        HistorySpringConfig.class,
        LocaleConfig.class,
        SecurityConfig.class,
        MailConfig.class
})
@PropertySource(value = "file:${user.home}/application.properties")
public class SpringRootConfiguration {

    public static final int THREAD_POOL_SIZE = 10;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        final FreeMarkerConfigurer conf = new FreeMarkerConfigurer();
        conf.setTemplateLoaderPath("/WEB-INF/views/");
        conf.setDefaultEncoding("UTF-8");
        return conf;
    }

    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() {
        return freeMarkerConfigurer().getConfiguration();
    }
}
