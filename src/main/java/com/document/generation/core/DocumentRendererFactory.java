package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DocumentRendererFactory {
    private final ObjectMapper objectMapper;

    public DocumentRendererFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DocumentRenderer getRenderer(RenderType type) {
        switch (type) {
            case MUSTACHE:
                return new MustacheRenderer(objectMapper);
            case FREEMARKER:
                try {
                    return new FreeMarkerRenderer(objectMapper);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to initialize Freemarker renderer", e);
                }
            default:
                throw new IllegalArgumentException("Unsupported renderer type: " + type);
        }
    }
}
