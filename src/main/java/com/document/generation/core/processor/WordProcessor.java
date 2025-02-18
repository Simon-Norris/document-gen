package com.document.generation.core.processor;

import com.document.generation.core.DocumentRendererFactory;
import com.document.generation.core.RenderType;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class WordProcessor implements DocumentProcessor {

    private final DocumentRendererFactory rendererFactory;

    public WordProcessor(DocumentRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    @Override
    public <T, R> R process(T template, JsonNode jsonNode, RenderType renderType) {

        if (template instanceof byte[] templateContent) {
            String extractTextFromDocx = extractTextFromDocx(templateContent);
            String response = this.rendererFactory.getRenderer(renderType).render(extractTextFromDocx, jsonNode);
            return (R) createWordDocument(response);
        } else if (template instanceof File templateContent) {
            String extractTextFromDocx = extractTextFromDocx(templateContent);
            String response = this.rendererFactory.getRenderer(renderType).render(extractTextFromDocx, jsonNode);
            return (R) createWordDocument(response);
        } else {
            throw new IllegalArgumentException("Template Not supported");
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

    private  String extractTextFromDocx(File templateContent) {
        try (FileInputStream inputStream = new FileInputStream(templateContent)) {
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
