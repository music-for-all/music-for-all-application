package com.musicforall.services;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.web.messages.WelcomeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service("messageService")
@Transactional
public class MessageService {

    public static final int PORT = 25;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void sendWelcomeMessage() throws MessagingException {
        MimeMessage welcomeMessage = WelcomeMessage.create(javaMailSender);
        try {
            javaMailSender.send(welcomeMessage);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void setPort(int Port) {
        javaMailSender.setPort(Port);
    }

    public void setHost(String Host) {
        javaMailSender.setHost(Host);
    }
}