package com.musicforall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * The configuration of the dispatcher servlet context.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.musicforall.web")
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Bean
    public FreeMarkerViewResolver viewResolver() {
        final FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setSuffix(".vm"); //to rename after integration
        resolver.setViewClass(FreeMarkerView.class);
        resolver.setContentType("text/html; charset=utf-8");
        resolver.setExposeSpringMacroHelpers(true);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public FreeMarkerConfigurer velocityConfigurer() {
        final FreeMarkerConfigurer conf = new FreeMarkerConfigurer();
        conf.setTemplateLoaderPath("/WEB-INF/velocity/");
        return conf;
    }
}
