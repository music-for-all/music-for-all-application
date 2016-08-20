package com.musicforall.services;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.web.messages.MessageRoot;
import com.musicforall.web.messages.WelcomeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void sendWelcomeMessage() throws MessagingException {
        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessage welcomeMessage = new WelcomeMessage(new MessageRoot(message)).getMimeMessage();

        try {
            javaMailSender.send(welcomeMessage);
        } catch (MailException ex) {
            LOG.error(ex.getMessage());
        }
    }

    public void setPort(final int port) {
        javaMailSender.setPort(port);
    }

    public void setHost(final String host) {
        javaMailSender.setHost(host);
    }
}