package com.document.generation.core.processor;

import com.document.generation.core.DocumentRendererFactory;
import com.document.generation.core.RenderType;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class HtmlProcessor implements DocumentProcessor{

    private final DocumentRendererFactory rendererFactory;

    public HtmlProcessor(DocumentRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    @Override
    public <T, R> R process(T template, JsonNode jsonNode, RenderType renderType) {
        if (template instanceof byte[] bytesContent) {
            String extractTextFromDocx = new String(bytesContent);
            String response = this.rendererFactory.getRenderer(renderType).render(extractTextFromDocx, jsonNode);
            return (R) response.getBytes();
        } else if (template instanceof File file) {
            String extractedText = extractContentFromHtml(file);
            String response = this.rendererFactory.getRenderer(renderType).render(extractedText, jsonNode);
            return (R) response.getBytes();
        } else {
            throw new IllegalArgumentException("Template type not supported");
        }
    }

    private String extractContentFromHtml(File input) {
        try {
            Document doc = Jsoup.parse(input, "UTF-8");
            return doc.html();
        } catch (IOException e) {
            throw new RuntimeException("Html File cannot be parsed");
        }
    }
}
