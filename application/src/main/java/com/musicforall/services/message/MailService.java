package com.musicforall.services.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by kgavrylchenko on 25.08.16.
 */
public class MailService {
    @Autowired
    private MailSender mailSender;

    public void send(SimpleMailMessage mail) {
        mailSender.send(mail);
    }
}
