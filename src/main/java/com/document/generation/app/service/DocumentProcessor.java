package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.entity.RichTemplate;
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

import javax.print.Doc;
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

    public  <T> byte[] generateDocument(T entity, String docType) {
        if (entity instanceof DocumentFile documentFile) {
            JsonNode jsonNode = JsonValidator.parseJson(objectMapper, documentFile.getJsonFileContent());

            boolean isWordDoc = docType.equals("docx");
            boolean isHtmlDoc = docType.equals("html");
            boolean isFtlDoc = docType.equals("ftl");

            if (isWordDoc) {
                return createWordDocument(renderTemplate(extractTextFromDocx(documentFile.getTemplateContent()), jsonNode, RenderType.MUSTACHE));
            } else if (isHtmlDoc) {
                String generatedContent = renderTemplate(new String(documentFile.getTemplateContent()), jsonNode, RenderType.MUSTACHE);
                return generatedContent.getBytes();
            } else if (isFtlDoc) {
                String generatedContent = renderTemplate(documentFile.getTemplateContent(), jsonNode, RenderType.FREEMARKER);
                return generatedContent.getBytes();
            } else {
                throw new IllegalArgumentException("Template not identified");
            }
        } else if (entity instanceof RichTemplate richTemplate) {
            JsonNode jsonNode = JsonValidator.parseJson(objectMapper, richTemplate.getJson());
            String generatedContent = renderTemplate(richTemplate.getContent(), jsonNode, RenderType.FREEMARKER);
            return generatedContent.getBytes();
        } else {
            throw new IllegalArgumentException("Entity not found");
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
    private <T> String renderTemplate(T template, JsonNode jsonNode, RenderType renderType) {
        DocumentRenderer renderer = this.rendererFactory.getRenderer(renderType);
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
