package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DocumentRendererFactory {
    private final ObjectMapper objectMapper;

    public DocumentRendererFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DocumentRenderer getRenderer(RenderType type) {
        if (Objects.requireNonNull(type) == RenderType.MUSTACHE) {
            return new MustacheRenderer(objectMapper);
        }
        throw new IllegalArgumentException("Unsupported renderer type: " + type);
    }
}
