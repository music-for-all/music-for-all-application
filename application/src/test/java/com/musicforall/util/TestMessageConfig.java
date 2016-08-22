package com.musicforall.util;

import com.musicforall.messages.TemplateMessage;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * @author Evgeniy on 21.08.2016.
 */
@Configuration
@ComponentScan({"com.musicforall.messages"})
public class TestMessageConfig {
    private static final int PORT = 3025;
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "password";

    @Bean
    public JavaMailSender javaMailSender() {
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("localhost");
        javaMailSender.setPort(PORT);
        javaMailSender.setUsername(EMAIL);
        javaMailSender.setPassword(PASSWORD);

        final Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.transport.protocol", "smtp");
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.debug", "true");
        javaMailSender.setJavaMailProperties(javaMailProperties);

        return javaMailSender;
    }

    @Bean(name = "email")
    public String email() {
        return EMAIL;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        final FreeMarkerConfigurer conf = new FreeMarkerConfigurer();
        conf.setTemplateLoaderPath("/WEB-INF/views/");
        conf.setDefaultEncoding("UTF-8");
        return conf;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public TemplateMessage newHtmlMessage(final Map<String, Object> params, final String templateName) {
        return new TemplateMessage(freeMarkerConfigurer().getConfiguration(), params, templateName);
    }
}
