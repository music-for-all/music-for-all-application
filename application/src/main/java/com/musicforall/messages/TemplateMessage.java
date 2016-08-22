package com.musicforall.messages;

import com.musicforall.util.MessageUtil;
import freemarker.template.Configuration;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author IliaNik on 12.08.2016.
 */

public class TemplateMessage extends JoinableMessage {

    private String templateName;
    private Configuration configuration;
    private Map<String, Object> params;

    public TemplateMessage(Configuration configuration, Map<String, Object> params, String templateName) {
        this.configuration = configuration;
        this.params = params;
        this.templateName = templateName;
    }

    @Override
    protected MimeMessage decorate(final MimeMessage message) throws MessagingException {
        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        final String text = MessageUtil.createMessageFromTemplate(configuration, params, templateName);
        helper.setText(text != null ? text : "", true);
        return helper.getMimeMessage();
    }
}