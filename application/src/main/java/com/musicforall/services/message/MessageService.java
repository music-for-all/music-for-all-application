package com.musicforall.services.message;

import com.musicforall.messages.MessageFactory;
import com.musicforall.messages.MessagePart;
import com.musicforall.messages.MessageRoot;
import com.musicforall.messages.TemplateMessage;
import com.musicforall.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

import static com.musicforall.util.SecurityUtil.currentUser;

/**
 * @author IliaNik on 12.08.2016.
 */
@Service
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageRoot messageRoot;

    @Autowired
    private MessageFactory messageFactory;

    public void sendWelcomeMessage() {
        final User user = currentUser();
        if (user == null) {
            return;
        }

        final MimeMessage message = javaMailSender.createMimeMessage();

        final Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());

        final TemplateMessage welcomeMessage = messageFactory.newTemplateMessage(params, "welcomeMessageTemplate.ftl");
        welcomeMessage
                .root(messageRoot)
                .setMessage(message);
        send(welcomeMessage);
    }

    private void send(final MessagePart message) {
        try {
            javaMailSender.send(message.getMimeMessage());
        } catch (MailException | MessagingException ex) {
            LOG.error("Message sending failed!", ex);
        }
    }
}