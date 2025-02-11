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
        return switch (type) {
            case MUSTACHE -> new MustacheRenderer(objectMapper);
            case FREEMARKER -> new FreeMarkerRenderer(objectMapper);
            default -> throw new IllegalArgumentException("Unsupported renderer type: " + type);
        };
    }
}
