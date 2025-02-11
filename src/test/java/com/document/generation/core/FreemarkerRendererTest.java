package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FreemarkerRendererTest {

    private FreeMarkerRenderer freemarkerRenderer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        freemarkerRenderer = new FreeMarkerRenderer(objectMapper);
    }

    @Test
    public void testRenderTemplate() {
        String templateString = "Hello, ${name}! You have ${count} unread messages.";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(templateString, dataModel);

        assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
    }

    @Test
    public void testRenderTemplateWithInvalidData() {
        String templateString = "Hello, ${name}!";

        Map<String, Object> dataModel = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            freemarkerRenderer.render(templateString.getBytes(), dataModel);
        });
    }
}
