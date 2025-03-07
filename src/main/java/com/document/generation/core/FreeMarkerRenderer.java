package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FreeMarkerRenderer implements DocumentRenderer {
    private final ObjectMapper objectMapper;
    private final Configuration freemarkerConfig;

    public FreeMarkerRenderer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setDefaultEncoding("UTF-8");

        // Set default template loader to the file system
        freemarkerConfig.setTemplateLoader(createTemplateLoader());
    }

    @Override
    public <T, R> R render(T template, Object... args) {
        Map<String, Object> context = objectMapper.convertValue(args[0], Map.class);
        if (context.isEmpty()) throw new IllegalArgumentException("No data were provided");

        Template freemarkerTemplate = null;
        String templateName = "template";

        try {
            if (template instanceof byte[]) {
                byte[] templateBytes = (byte[]) template;
                String templateString = new String(templateBytes, StandardCharsets.UTF_8);
                freemarkerTemplate = new Template(templateName, templateString, freemarkerConfig);
            } else if (template instanceof String) {
                String templateStr = (String) template;
                if (isClasspathResource(templateStr)) {
                    freemarkerTemplate = freemarkerConfig.getTemplate(templateStr.replace("classpath:", ""));
                } else {
                    freemarkerTemplate = new Template(templateName, templateStr, freemarkerConfig);
                }
            } else if (template instanceof File) {
                File templateFile = (File) template;
                freemarkerTemplate = new Template(templateName,
                        new InputStreamReader(new FileInputStream(templateFile), StandardCharsets.UTF_8),
                        freemarkerConfig);
            } else if (template instanceof InputStream) {
                InputStream templateStream = (InputStream) template;
                freemarkerTemplate = new Template(templateName,
                        new InputStreamReader(templateStream, StandardCharsets.UTF_8),
                        freemarkerConfig);
            } else {
                throw new IllegalArgumentException("Unsupported template type: " + template.getClass().getName());
            }

            StringWriter writer = new StringWriter();
            freemarkerTemplate.process(context, writer);

            return (R) writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error processing Freemarker template", e);
        }
    }

    private boolean isClasspathResource(String templateStr) {
        return templateStr.startsWith("classpath:");
    }

    private TemplateLoader createTemplateLoader() {
        return new ClassTemplateLoader(getClass(), "/");
    }
}
