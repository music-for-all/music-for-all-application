package com.musicforall.web.messages;

/**
 * @author IliaNik on 12.08.2016.
 */

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.musicforall.util.SecurityUtil.currentUser;

public final class WelcomeMessage implements MessagePart {

    private final MessagePart part;

    public WelcomeMessage(MessagePart part) {
        this.part = part;
    }

    public static String text(final String username) {
        return "<html><body>" +
                "<div style='border:4px ridge red;text-align:center;" +
                "font-family:Verdana,Arial,Helvetica,sans-serif'> " +
                "<h1 style='color:red'>Welcome," + username + "!</h1><br>" +
                " <p>You have just been registered in the best music player</p>" +
                "<h2>Congratulation!</h2>" +
                "</div>" +
                "</body></html>";
    }

    private MimeMessage decorate(final MimeMessage message) throws MessagingException {
        final String username = currentUser().getUsername();

        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(WelcomeMessage.text(username), true);

        return helper.getMimeMessage();
    }

    @Override
    public MimeMessage getMimeMessage() throws MessagingException {
        return decorate(part.getMimeMessage());
    }
}