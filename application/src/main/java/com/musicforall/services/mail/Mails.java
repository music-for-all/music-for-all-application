package com.musicforall.services.mail;

import com.musicforall.dto.mail.Mail;
import com.musicforall.model.user.User;
import com.musicforall.services.template.TemplateService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kgavrylchenko on 25.08.16.
 */
@Component
public class Mails {
    @Autowired
    private TemplateService templateService;

    @Autowired
    @Qualifier("email")
    private String emailFrom;

    public Mail welcomeMail(final User user) {
        final Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        return Mail.build()
                .to(user.getEmail())
                .from(emailFrom)
                .subject("WELCOME")
                .text(mailText("welcomeMessageTemplate.ftl", params))
                .get();
    }

    private String mailText(String template, Map<String, Object> params) {
        String mailText;
        try {
            mailText = templateService.from(template, params);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }

        return mailText;
    }
}
