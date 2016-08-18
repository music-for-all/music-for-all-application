package com.musicforall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author IliaNik on 16.08.2016.
 */
@Configuration
@Profile("dev")
public class MessageConfigDev {
    public static final int PORT = 25;

    @Bean
    public JavaMailSender javaMailSender() {

        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.transport.protocol", "smtp");
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.debug", "true");

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(PORT);
        javaMailSender.setUsername("musicforall07@gmail.com");
        javaMailSender.setPassword("PolyInkExt");

        javaMailSender.setJavaMailProperties(javaMailProperties);

        return javaMailSender;
    }
}
