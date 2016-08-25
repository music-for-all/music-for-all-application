package com.musicforall.services.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by kgavrylchenko on 25.08.16.
 */
@Service
public class MailService {
    @Autowired
    private MailSender mailSender;

    public void send(SimpleMailMessage mail) {
        Objects.requireNonNull(mail, "mail must not be null!");
        mailSender.send(mail);
    }
}