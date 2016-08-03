package com.musicforall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;


public class LocaleConfig {

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:locale");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
