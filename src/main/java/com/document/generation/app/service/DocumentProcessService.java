package com.document.generation.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class DocumentProcessService {

    public static byte[] generateWordDocument(byte[] templateContent, byte[] jsonFileContent) throws Exception {
        // Convert JSON content to a Map
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonFileContent);

        // Load Word document
        ByteArrayInputStream templateStream = new ByteArrayInputStream(templateContent);
        XWPFDocument document = new XWPFDocument(templateStream);

        // Replace placeholders in paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replacePlaceholders(paragraph, jsonNode);
        }

        // Replace placeholders in tables
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replacePlaceholders(paragraph, jsonNode);
                    }
                }
            }
        }

        // Write output document to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    private static void replacePlaceholders(XWPFParagraph paragraph, JsonNode jsonNode) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null && text.contains("{{")) {
                String replacedText = replaceTextWithJsonData(text, jsonNode);
                run.setText(replacedText, 0);
            }
        }
    }

    private static String replaceTextWithJsonData(String text, JsonNode jsonNode) {
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            String value = field.getValue().isTextual() ? field.getValue().asText() : field.getValue().toString();
            text = text.replace("{{" + key + "}}", value);
        }
        return text;
    }

}
