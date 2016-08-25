package com.musicforall.services.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.Map;


/**
 * Created by kgavrylchenko on 25.08.16.
 */
public class TemplateService {
    @Autowired
    private Configuration configuration;

    public String from(String templateName, Map<String, Object> params) throws Exception {
        Template template = configuration.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(params, writer);
        return writer.toString();
    }

}
