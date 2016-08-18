package com.musicforall.web.messages;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.model.User;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public final class WelcomeMessage {

    private WelcomeMessage() {

    }

    public static MimeMessage decorate(MimeMessage message, User user) throws MessagingException {

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setFrom("musicforall07@gmail.com");
        helper.setSubject("Music For All");
        helper.setText(getWelcomeText(user.getUsername()), true);

        return message;
    }

    public static String getWelcomeText(final String username) {
        return "<html><body>" +
                "<div style='border:4px ridge red;text-align:center;" +
                "font-family:Verdana,Arial,Helvetica,sans-serif'> " +
                "<h1 style='color:red'>Welcome," + username + "!</h1><br>" +
                " <p>You have just been registered in the best music player</p>" +
                "<h2>Congratulation!</h2>" +
                "</div>" +
                "</body></html>";
    }
}