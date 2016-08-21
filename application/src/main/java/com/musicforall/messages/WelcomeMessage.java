package com.musicforall.messages;

import com.musicforall.util.MessageUtil;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

import static com.musicforall.util.SecurityUtil.currentUser;

/**
 * @author IliaNik on 12.08.2016.
 */

@Component
public class WelcomeMessage extends JoinableMessage {

    @Autowired
    private Configuration configuration;

    public String message(final String username) {
        final Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("username", username);

        return MessageUtil.createMessageFromTemplate(configuration, templateParams, "welcomeMessageTemplate.ftl");
    }

    @Override
    protected MimeMessage decorate(final MimeMessage message) throws MessagingException {
        final String username = currentUser().getUsername();

        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setText(message(username), true);
        return helper.getMimeMessage();
    }
}