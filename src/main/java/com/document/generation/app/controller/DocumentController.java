package com.document.generation.app.controller;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.service.DocumentProcessService;
import com.document.generation.app.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
@CrossOrigin("*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

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

    @GetMapping("/download/word/{id}")
    public ResponseEntity<byte[]> downloadWord(@PathVariable Long id) {
        Optional<DocumentFile> optionalDocument = documentService.findById(id);
        if (optionalDocument.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DocumentFile documentFile = optionalDocument.get();
        try {
            byte[] processedDocument = DocumentProcessService.generateWordDocument(documentFile.getTemplateContent(), documentFile.getJsonFileContent());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated.docx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(processedDocument);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
