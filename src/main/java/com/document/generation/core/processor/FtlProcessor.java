package com.document.generation.core.processor;

import com.document.generation.core.DocumentRendererFactory;
import com.document.generation.core.RenderType;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class FtlProcessor implements DocumentProcessor{

    private final DocumentRendererFactory rendererFactory;

    public FtlProcessor(DocumentRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    @Override
    public <T, R> R process(T template, JsonNode jsonNode, RenderType renderType) {
        renderType = RenderType.FREEMARKER;
        String response = this.rendererFactory.getRenderer(renderType).render(template, jsonNode);
        return (R) response.getBytes();
    }
}
