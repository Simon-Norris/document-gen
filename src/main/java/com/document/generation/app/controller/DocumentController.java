package com.document.generation.app.controller;

import com.document.generation.app.dto.RichTemplateRequest;
import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.entity.RichTemplate;
import com.document.generation.app.service.DocumentService;
import com.document.generation.app.service.GenerationService;
import com.document.generation.app.service.RichTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
@CrossOrigin("*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private GenerationService generationService;

    @Autowired
    private RichTemplateService richTemplateService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("template") MultipartFile templateFile,
            @RequestParam("jsonFile") MultipartFile jsonFile
    ) {
        if (templateFile.isEmpty() || jsonFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload both Word and JSON files!");
        }

        try {
            DocumentFile documentFile = documentService.uploadDocument(templateFile, jsonFile);
            return ResponseEntity.ok("File uploaded successfully with ID: " + documentFile.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/download/{format}/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String format, @PathVariable Long id) {
        Optional<DocumentFile> optionalDocument = documentService.findById(id);
        if (optionalDocument.isEmpty()) return ResponseEntity.notFound().build();

        DocumentFile documentFile = optionalDocument.get();
        String savedFormat;

        boolean isWordDoc = documentFile.getTemplateType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        boolean isHtmlDoc = documentFile.getTemplateType().equalsIgnoreCase("text/html");

        if (isWordDoc && format.equals("word")) {
            savedFormat = "docx";
        } else if (isHtmlDoc && format.equals("html")) {
            savedFormat = "html";
        } else if (format.equals("ftl")) {
            savedFormat="ftl";
        } else {
            return ResponseEntity.status(400).body(null);
        }

        try {
            byte[] processedDocument = generationService.generateDocument(documentFile, savedFormat);

            String fileName = "generated." + savedFormat.toLowerCase();
            MediaType contentType;

            switch (savedFormat.toLowerCase()) {
                case "html":
                    contentType = MediaType.TEXT_HTML;
                    break;
                case "docx":
                    contentType = MediaType.APPLICATION_OCTET_STREAM;
                    break;
                case "ftl":
                    contentType = MediaType.TEXT_HTML;
                    fileName="generated.html";
                    break;
                default:
                    return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(contentType)
                    .body(processedDocument);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
