package com.musicforall.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author ENikolskiy.
 */
@Configuration
@ComponentScan("com.musicforall.files.manager")
@PropertySource(value = "file:${user.home}/application.properties")
public class FileApiSpringConfig {

    @Autowired
    private Environment env;

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSizePerFile(Long.valueOf(env.getRequiredProperty("web.max_upload_size_per_file")));
        return resolver;
    }
}
