package com.musicforall.services;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.web.messages.WelcomeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service("messageService")
@Transactional
public class MessageService {

    public static final int PORT = 25;

    private JavaMailSenderImpl javaMailSender = javaMailSender();

    public boolean sendWelcomeMessage() throws MessagingException {
        MimeMessage welcomeMessage = WelcomeMessage.create(javaMailSender);
        try {
            javaMailSender.send(welcomeMessage);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public JavaMailSenderImpl javaMailSender() {

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