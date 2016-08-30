package com.musicforall.services.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

/**
 * Created by kgavrylchenko on 25.08.16.
 */
@Service
public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @EventListener
    public void send(Mail mail) {
        Objects.requireNonNull(mail, "mail must not be null!");
        try {
            mailSender.send(toMimeMessage(mail));
        } catch (MessagingException e) {
            LOG.error("Message sending failed!", e);
        }
    }

    private MimeMessage toMimeMessage(final Mail mail) throws MessagingException {
        final MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage());
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getFrom());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText(), true);
        return helper.getMimeMessage();
    }
}