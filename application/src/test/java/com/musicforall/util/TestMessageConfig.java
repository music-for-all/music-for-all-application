package com.musicforall.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Evgeniy on 21.08.2016.
 */
@Configuration
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
        return new FreeMarkerConfigurer();
    }

    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() throws IOException {
        final freemarker.template.Configuration config = freeMarkerConfigurer().getConfiguration();
        final Path resourcePath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources");

        config.setDirectoryForTemplateLoading(resourcePath.toFile());
        return config;
    }
}
