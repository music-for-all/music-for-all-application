package com.musicforall.config;

import com.musicforall.messages.TemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * @author IliaNik on 16.08.2016.
 */
@Configuration
@ComponentScan({"com.musicforall.messages"})

public class MailConfig {
    public static final int PORT = 25;

    @Autowired
    private Environment env;

    @Autowired
    private FreeMarkerConfigurer configurer;

    @Bean
    public JavaMailSender javaMailSender() {
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

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public TemplateMessage newTemplateMessage(final Map<String, Object> params, final String templateName) {
        return new TemplateMessage(configurer.getConfiguration(), params, templateName);
    }
}
