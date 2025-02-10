package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.utils.JsonValidator;
import com.document.generation.core.DocumentRenderer;
import com.document.generation.core.DocumentRendererFactory;
import com.document.generation.core.RenderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class DocumentProcessor {

    private final ObjectMapper objectMapper;
    private final DocumentRendererFactory rendererFactory;

    public DocumentProcessor(DocumentRendererFactory rendererFactory, ObjectMapper objectMapper) {
        this.rendererFactory = rendererFactory;
        this.objectMapper = objectMapper;
    }

    public  byte[] generateDocument(DocumentFile documentFile, String docType) {
        JsonNode jsonNode = JsonValidator.parseJson(objectMapper, documentFile.getJsonFileContent());

        boolean isWordDoc = docType.equals("docx");
        boolean isHtmlDoc = docType.equals("html");

        if (isWordDoc) {
            return createWordDocument(renderTemplate(extractTextFromDocx(documentFile.getTemplateContent()), jsonNode));
        } else if (isHtmlDoc) {
            String generatedContent = renderTemplate(new String(documentFile.getTemplateContent()), jsonNode);
            return generatedContent.getBytes();
        } else {
            throw new IllegalArgumentException("Template not identified");
        }
    }

    private  String extractTextFromDocx(byte[] templateContent) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(templateContent)) {
            XWPFDocument doc = new XWPFDocument(inputStream);
            StringBuilder extractedText = new StringBuilder();

            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                extractedText.append(paragraph.getText()).append("\n");
            }

            return extractedText.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to extract text from .docx file", e);
        }
    }
    private  String renderTemplate(String template, JsonNode jsonNode) {
        DocumentRenderer renderer = this.rendererFactory.getRenderer(RenderType.MUSTACHE);
        return renderer.render(template, jsonNode);
    }

    private  byte[] createWordDocument(String content) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFDocument doc = new XWPFDocument();

            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(content);

            doc.write(out);

            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
