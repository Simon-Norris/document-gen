package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FreeMarkerRendererTest {

    private FreeMarkerRenderer freemarkerRenderer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        freemarkerRenderer = new FreeMarkerRenderer(objectMapper);
    }

    @Test
    public void testRenderTemplateStringCase() {
        String templateString = "Hello, ${name}! You have ${count} unread messages.";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(templateString, dataModel);

        assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
    }

    @Test
    public void testRenderTemplateByteCase() {
        String templateString = "Hello, ${name}! You have ${count} unread messages.";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(templateString.getBytes(), dataModel);

        assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
    }

    @Test
    public void testRenderTemplateFileCase(@TempDir File tempDir) throws IOException {
        File tempFile = new File(tempDir, "template.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Hello, ${name}! You have ${count} unread messages.");
        }

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(tempFile, dataModel);

        assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
    }

    @Test
    public void testRenderTemplateInputStreamCase(@TempDir File tempDir) throws IOException {
        File tempFile = new File(tempDir, "template.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Hello, ${name}! You have ${count} unread messages.");
        }

        try (InputStream inputStream = new FileInputStream(tempFile)) {
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("name", "John");
            dataModel.put("count", 5);

            String renderedOutput = freemarkerRenderer.render(inputStream, dataModel);

            assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
        }
    }

    @Test
    public void testRenderTemplateClasspathResource() {
        String templatePath = "classpath:templates/hello.ftl";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(templatePath, dataModel);

        assertEquals("Hello Dear John! You have 5 unread messages.\r\n", renderedOutput);
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
