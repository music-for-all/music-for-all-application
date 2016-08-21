package com.musicforall.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author Evgeniy on 21.08.2016.
 */
public final class MessageUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MessageUtil.class);

    private MessageUtil() {
    }

    public static String createMessageFromTemplate(final Configuration config,
                                                   final Map<String, Object> params,
                                                   final String templateName) {
        try {
            final Template template = config.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (TemplateException | IOException ex) {
            LOG.error("Template creation failed!", ex);
            return null;
        }
    }
}
