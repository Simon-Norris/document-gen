package com.document.generation.app.controller;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{format}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String format) throws Exception {
        Map<String, Object> model = Map.of(
            "name", "John Doe",
            "items", List.of(
                Map.of("name", "Item 1", "quantity", "10", "price", "100"),
                Map.of("name", "Item 2", "quantity", "20", "price", "200")
            )
        );

        byte[] content = "pdf".equalsIgnoreCase(format) ? null : documentService.generateWord(model);

        String filename = "document." + format;
        MediaType mediaType = "pdf".equalsIgnoreCase(format)
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(content);
    }
}
