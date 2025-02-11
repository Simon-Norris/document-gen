package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class FreeMarkerRenderer implements DocumentRenderer {
    private final ObjectMapper objectMapper;
    private final Configuration freemarkerConfig;

    public FreeMarkerRenderer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setDefaultEncoding("UTF-8");
    }

    @Override
    public <T, R> R render(T template, Object... args) {
        if (template == null) throw new IllegalArgumentException("Template must be provided.");

        Map<String, Object> context = objectMapper.convertValue(args[0], Map.class);
        StringWriter writer = new StringWriter();

        try {
            Template freemarkerTemplate = null;

            switch (template.getClass().getName()) {
                case "byte[]":
                    byte[] templateBytes = (byte[]) template;
                    String templateString = new String(templateBytes, StandardCharsets.UTF_8);
                    freemarkerTemplate = new Template("template", templateString, freemarkerConfig);
                    break;
                case "java.lang.String":
                    String templateStr = (String) template;
                    freemarkerTemplate = freemarkerConfig.getTemplate(templateStr);
                    break;
                case "java.io.File":
                    File templateFile = (File) template;
                    freemarkerTemplate = new Template("template",
                            new InputStreamReader(new FileInputStream(templateFile), StandardCharsets.UTF_8), freemarkerConfig);
                    break;
                case "java.io.InputStream":
                    InputStream templateStream = (InputStream) template;
                    freemarkerTemplate = new Template("template",
                            new InputStreamReader(templateStream, StandardCharsets.UTF_8), freemarkerConfig);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported template type: " + template.getClass().getName());
            }

            freemarkerTemplate.process(context, writer);

            return (R) writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error processing Freemarker template", e);
        }
    }
}
