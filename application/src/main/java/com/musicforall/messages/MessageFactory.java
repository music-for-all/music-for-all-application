package com.musicforall.messages;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ENikolskiy.
 */
@Component
public class MessageFactory {
    @Lookup
    public TemplateMessage newTemplateMessage(final Map<String, Object> params, final String templateName) {
        return new TemplateMessage(new Configuration(), params, templateName);
    }
}