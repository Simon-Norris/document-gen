package com.document.generation.core.processor;

import com.document.generation.core.DocumentRendererFactory;
import com.document.generation.core.RenderType;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class SimpleProcessor implements DocumentProcessor{
    private final DocumentRendererFactory rendererFactory;

    public SimpleProcessor(DocumentRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    @Override
    public <T, R> R process(T template, JsonNode jsonNode, RenderType renderType) {
        return this.rendererFactory.getRenderer(RenderType.FREEMARKER).render(template, jsonNode);
    }
}
