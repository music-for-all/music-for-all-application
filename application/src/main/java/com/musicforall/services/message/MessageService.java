package com.musicforall.services.message;

import com.musicforall.messages.MessageRoot;
import com.musicforall.messages.WelcomeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author IliaNik on 12.08.2016.
 */
@Service
@Transactional
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private MessageRoot messageRoot;

    @Autowired
    private WelcomeMessage welcomeMessage;

    public void sendWelcomeMessage() {
        final MimeMessage message = javaMailSender.createMimeMessage();
        welcomeMessage
                .root(messageRoot)
                .setMessage(message);
        try {
            javaMailSender.send(welcomeMessage.getMimeMessage());
        } catch (MailException | MessagingException ex) {
            LOG.error("Message sending failed!", ex);
        }
    }
}