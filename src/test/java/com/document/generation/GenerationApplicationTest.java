package com.document.generation;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.service.DocumentProcessor;
import com.document.generation.core.DocumentRendererFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GenerationApplicationTest {

    @Autowired
    private DocumentProcessor documentProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DocumentRendererFactory rendererFactory;

    private DocumentFile testDocumentFile;


    @BeforeEach
    public void setUp() {
        testDocumentFile = new DocumentFile();
        testDocumentFile.setJsonFileContent("{\"name\": \"John\", \"count\": 5}".getBytes());
        testDocumentFile.setTemplateContent("Hello, ${name}! You have ${count} unread messages.".getBytes());
    }

    @Test
    public void testGenerateDocument() {
        byte[] generatedDocument = documentProcessor.generateDocument(testDocumentFile, "html");

        assertNotNull(generatedDocument, "The generated document should not be null.");
    }

    @Test
    public void testDocumentProcessor() {
        assertNotNull(documentProcessor, "DocumentProcessor should be loaded in the application context.");
    }

    @Test
    public void testRendererFactory() {
        assertNotNull(rendererFactory, "DocumentRendererFactory should be loaded in the application context.");
    }
}
