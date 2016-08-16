package com.musicforall.web.messages;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.util.SecurityUtil;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public final class WelcomeMessage {

    private WelcomeMessage() {
    }

    public static MimeMessage create(JavaMailSenderImpl mailSender) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(SecurityUtil.currentUser().getEmail());
        helper.setFrom("musicforall07@gmail.com");
        helper.setSubject("Music For All");
        helper.setText(
                "<html><body><h1>Welcome, " + SecurityUtil.currentUser().getUsername() + "<h1></body></html>", true);

        return message;
    }
}