package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FreeMarkerRenderer implements DocumentRenderer {
    private final ObjectMapper objectMapper;
    private final Configuration freemarkerConfig;

    public FreeMarkerRenderer(ObjectMapper objectMapper, Configuration freemarkerConfig) {
        this.objectMapper = objectMapper;
        this.freemarkerConfig = freemarkerConfig;
    }

    @Override
    public <T, R> R render(T template, Object... args) {
        Map<String, Object> context = objectMapper.convertValue(args[0], Map.class);
        if (context.isEmpty()) throw new IllegalArgumentException("No data were provided");

        Template freemarkerTemplate = null;
        String templateName = "template";

        try {
            if (template instanceof byte[] templateBytes) {
                String templateString = new String(templateBytes, StandardCharsets.UTF_8);
                freemarkerTemplate = new Template(templateName, templateString, freemarkerConfig);
            } else if (template instanceof String templateStr) {
                if (isClasspathResource(templateStr)) {
                    freemarkerTemplate = freemarkerConfig.getTemplate(templateStr.replace("classpath:", ""));
                } else {
                    freemarkerTemplate = new Template(templateName, templateStr, freemarkerConfig);
                }
            } else if (template instanceof File templateFile) {
                freemarkerTemplate = new Template(templateName,
                        new InputStreamReader(new FileInputStream(templateFile), StandardCharsets.UTF_8),
                        freemarkerConfig);
            } else if (template instanceof InputStream templateStream) {
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

}
