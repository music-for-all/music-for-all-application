package com.musicforall.files;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author ENikolskiy.
 */
@Configuration
@ComponentScan("com.musicforall.files.manager")
public class FileApiSpringConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
//        resolver.setMaxUploadSizePerFile(Long.valueOf(env.getRequiredProperty("web.max_upload_size_per_file")));
        resolver.setMaxUploadSizePerFile(50000000);
        return resolver;
    }
}
