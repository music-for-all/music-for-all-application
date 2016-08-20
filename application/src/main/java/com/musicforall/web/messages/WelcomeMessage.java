package com.musicforall.web.messages;

/**
 * @author IliaNik on 12.08.2016.
 */

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.musicforall.util.SecurityUtil.currentUser;

public final class WelcomeMessage implements MessagePart {

    private static final Logger LOG = LoggerFactory.getLogger(WelcomeMessage.class);

    private final MessagePart part;

    public WelcomeMessage(MessagePart part) {
        this.part = part;
    }

    public static String text(final String username) throws IOException {

        Configuration freemarkerConfiguration = new Configuration();

        FileTemplateLoader templateLoader = new FileTemplateLoader(new File("src/main/resources"));
        freemarkerConfiguration.setTemplateLoader(templateLoader);

        Map<String, Object> freeMarkerTemplateMap = new HashMap<>();
        freeMarkerTemplateMap.put("username", username);

        Template template = freemarkerConfiguration.getTemplate("welcomeMessageTemplate.ftl");

        try {
            String messageText =
                    FreeMarkerTemplateUtils.processTemplateIntoString(template, freeMarkerTemplateMap);
            return messageText;
        } catch (TemplateException ex) {
            LOG.error(ex.getMessage());
        }
        return null;
    }

    private MimeMessage decorate(final MimeMessage message) throws MessagingException {
        final String username = currentUser().getUsername();

        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        try {
            helper.setText(WelcomeMessage.text(username), true);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        return helper.getMimeMessage();
    }

    @Override
    public MimeMessage getMimeMessage() throws MessagingException {
        return decorate(part.getMimeMessage());
    }
}