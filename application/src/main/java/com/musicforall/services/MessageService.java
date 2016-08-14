package com.musicforall.services;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.util.SecurityUtil;
import com.musicforall.web.messages.WelcomeMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

@Service("messageService")
@Transactional
public class MessageService {

    public void sendWelcomeMessage() {

        String to = SecurityUtil.currentUser().getEmail();

        String from = "musicforall07@gmail.com";
        final String username = "musicforall07@gmail.com";
        final String password = "PolyInkExt";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = WelcomeMessage.create(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            Transport.send(message);  // Send message
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
