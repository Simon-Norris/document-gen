package com.document.generation.app.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.Map;

public class DocumentProcessor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Main method to generate a Word document from template and JSON data
    public static byte[] generateDocument(byte[] templateContent, byte[] jsonData) {
        // Parse JSON data
        JsonNode jsonNode = parseJson(jsonData);

        // Extract text from the .docx template (this will be used as the Mustache template)
        String template = extractTextFromDocx(templateContent);

        // Process the Mustache template
        String renderedContent = renderTemplate(template, jsonNode);

        // Create Word document from rendered content
        return createWordDocument(renderedContent);
    }

    // Parse the JSON data into a JsonNode
    private static JsonNode parseJson(byte[] jsonData) {
        try {
            return objectMapper.readTree(jsonData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }

    // Extract text content from a .docx template
    private static String extractTextFromDocx(byte[] templateContent) {
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

    // Render the Mustache template with the provided JSON data
    private static String renderTemplate(String template, JsonNode jsonNode) {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(template), "template");

        // Convert the JSON into a map for Mustache processing
        Map<String, Object> context = objectMapper.convertValue(jsonNode, Map.class);
        StringWriter writer = new StringWriter();

        mustache.execute(writer, context);

        return writer.toString();
    }

    // Create a Word document from the rendered Mustache content
    private static byte[] createWordDocument(String content) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFDocument doc = new XWPFDocument();

            // Create a paragraph and set the text from the rendered content
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(content);

            // Write the document to the output stream
            doc.write(out);

            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    // Example usage
    public static void main(String[] args) {
        try {
            // Load your template from a file (Mustache template)
            byte[] templateContent = new FileInputStream("C:\\Workspace\\Documents\\RD\\DOC-GEN\\test.docx").readAllBytes();

            // Example JSON data
            String jsonData = """
                    {
                        "docNumber": "INV-20240201",
                        "name": "John Doe",
                        "gender": "Male",
                        "contactDetails": {
                            "name": "Smith",
                            "phone": "123-456-7890",
                            "gender": "Female"
                        },
                        "items": [
                            { "item_name": "Laptop", "item_quantity": "1", "item_price": "1000", "item_total": "1000" },
                            { "item_name": "Mouse", "item_quantity": "2", "item_price": "50", "item_total": "100" },
                            { "item_name": "Keyboard", "item_quantity": "1", "item_price": "75", "item_total": "75" }
                        ],
                        "total_amount": "1175",
                        "payment_status": "Paid"
                    }
                """;

            // Generate the document
            byte[] generatedDoc = generateDocument(templateContent, jsonData.getBytes());

            // Save the generated Word document to a file
            try (FileOutputStream fos = new FileOutputStream("C:\\Users\\ACER\\Downloads\\output.docx")) {
                fos.write(generatedDoc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
