package com.musicforall.web.messages;

import com.musicforall.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.musicforall.util.SecurityUtil.currentUser;

/**
 * @author IliaNik on 19.08.2016.
 */
@Component
public final class MessageTemplate implements MessagePart {

    private final MimeMessage message;

    @Autowired
    @Qualifier("email")
    private String email;

    public MessageTemplate(MimeMessage message) {
        this.message = message;
    }

    @Override
    public MimeMessage getMimeMessage() throws MessagingException {
        final User user = currentUser();

        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setFrom(email);
        helper.setSubject("Music For All");

        return helper.getMimeMessage();
    }
}
