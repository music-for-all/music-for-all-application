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

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    public boolean sendWelcomeMessage() throws MessagingException {
        MimeMessage welcomeMessage = WelcomeMessage.create(javaMailSenderImpl);
        try {
            javaMailSenderImpl.send(welcomeMessage);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
        return true;
    }

}