package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.repo.DocumentFileRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentFileRepository documentFileRepository;

    public DocumentServiceImpl(DocumentFileRepository documentFileRepository) {
        this.documentFileRepository = documentFileRepository;
    }

    /**
     * Uploads and stores a document in the database.
     */
    @Override
    public DocumentFile uploadDocument(MultipartFile templateFile, MultipartFile jsonFile) throws IOException {
        DocumentFile doc = new DocumentFile();
        doc.setTemplateName(templateFile.getOriginalFilename());
        doc.setTemplateType(templateFile.getContentType());
        doc.setTemplateContent(templateFile.getBytes());

        doc.setJsonFileName(jsonFile.getOriginalFilename());
        doc.setJsonFileType(jsonFile.getContentType());
        doc.setJsonFileContent(jsonFile.getBytes());

        return documentFileRepository.save(doc);
    }

    /**
     * Generates a Word document dynamically with tables and text fields.
     */
    public byte[] generateWord(Map<String, Object> model) throws IOException {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Hello, " + model.getOrDefault("name", "User") + "!");

            document.createParagraph().createRun().addBreak();

            // Add Table (if available)
            if (model.containsKey("items")) {
                List<Map<String, String>> items = (List<Map<String, String>>) model.get("items");

                XWPFTable table = document.createTable();
                XWPFTableRow headerRow = table.getRow(0);
                headerRow.getCell(0).setText("Item Name");
                headerRow.addNewTableCell().setText("Quantity");
                headerRow.addNewTableCell().setText("Price");

                for (Map<String, String> item : items) {
                    XWPFTableRow row = table.createRow();
                    row.getCell(0).setText(item.getOrDefault("name", ""));
                    row.getCell(1).setText(item.getOrDefault("quantity", ""));
                    row.getCell(2).setText(item.getOrDefault("price", ""));
                }
            } else {
                document.createParagraph().createRun().setText("No items available.");
            }

            document.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
