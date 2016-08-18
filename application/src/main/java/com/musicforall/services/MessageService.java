package com.musicforall.services;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.web.messages.WelcomeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service("messageService")
@Transactional
public class MessageService {

    public static final int PORT = 25;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendWelcomeMessage() throws MessagingException {
        MimeMessage welcomeMessage = WelcomeMessage.create(javaMailSender);
        try {
            javaMailSender.send(welcomeMessage);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());

        }

    }


}