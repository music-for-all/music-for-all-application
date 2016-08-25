package com.musicforall.services.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;


/**
 * Created by kgavrylchenko on 25.08.16.
 */
@Service
public class TemplateService {
    @Autowired
    private Configuration configuration;

    public String from(String templateName, Map<String, Object> params) throws Exception {
        final Template template = configuration.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
    }
}
