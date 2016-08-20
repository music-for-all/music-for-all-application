package com.musicforall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author IliaNik on 16.08.2016.
 */
@Configuration
public class MessageConfig {
    public static final int PORT = 25;

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSenderImpl javaMailSender() {

        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(PORT);
        javaMailSender.setUsername(env.getRequiredProperty("message.email"));
        javaMailSender.setPassword(env.getRequiredProperty("message.password"));

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
        return env.getRequiredProperty("message.email");
    }

}
