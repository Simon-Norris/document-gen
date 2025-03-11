package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import org.springframework.stereotype.Component;

@Component
public class DocumentRendererFactory {
    private final ObjectMapper objectMapper;
    private final Configuration freemarkerConfiguration;

    public DocumentRendererFactory(ObjectMapper objectMapper, Configuration freemarkerConfiguration) {
        this.objectMapper = objectMapper;
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    public DocumentRenderer getRenderer(RenderType type) {
        switch (type) {
            case MUSTACHE:
                return new MustacheRenderer(objectMapper);
            case FREEMARKER:
                return new FreeMarkerRenderer(objectMapper, freemarkerConfiguration);
            default:
                throw new IllegalArgumentException("Unsupported renderer type: " + type);
        }
    }
}
