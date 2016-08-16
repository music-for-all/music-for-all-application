package com.musicforall.services;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("messageService")
@Transactional
public class MessageService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private SimpleMailMessage templateMessage;

    public void sendWelcomeMessage() {

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(SecurityUtil.currentUser().getEmail());
        msg.setText(
                "Dear " + SecurityUtil.currentUser().getUsername() +
                        ", thank you for placing order. Your order number is ");

        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {

            System.err.println(ex.getMessage());
        }
    }

}