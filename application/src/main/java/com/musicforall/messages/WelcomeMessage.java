package com.musicforall.messages;

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

/**
 * @author IliaNik on 12.08.2016.
 */

public final class WelcomeMessage implements MessagePart {

    private static final Logger LOG = LoggerFactory.getLogger(WelcomeMessage.class);

    private final MessagePart part;

    public WelcomeMessage(MessagePart part) {
        this.part = part;
    }

    public static String text(final String username) throws IOException {
        final Configuration config = new Configuration();
        final FileTemplateLoader templateLoader = new FileTemplateLoader(new File("./WEB-INF/views/"));
        config.setTemplateLoader(templateLoader);

        final Map<String, Object> freeMarkerTemplateMap = new HashMap<>();
        freeMarkerTemplateMap.put("username", username);

        final Template template = config.getTemplate("welcomeMessageTemplate.ftl");

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, freeMarkerTemplateMap);
        } catch (TemplateException ex) {
            LOG.error("Template creation failed!", ex);
            return null;
        }
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