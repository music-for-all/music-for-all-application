package com.musicforall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.musicforall.web")
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Bean
    public VelocityViewResolver viewResolver() {
        final VelocityViewResolver resolver = new VelocityViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".vm");
        resolver.setViewClass(VelocityView.class);
        resolver.setContentType("text/html; charset=utf-8");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public VelocityConfigurer velocityConfigurer() {
        final VelocityConfigurer conf = new VelocityConfigurer();
        conf.setResourceLoaderPath("/WEB-INF/velocity/");
        return conf;
    }
}
