package com.document.generation.app.controller;

import com.document.generation.app.dto.RichTemplateRequest;
import com.document.generation.app.entity.RichTemplate;
import com.document.generation.app.service.RichTemplateService;
import com.document.generation.app.utils.JsonValidator;
import com.document.generation.core.RenderType;
import com.document.generation.core.processor.DocumentProcessorFactory;
import com.document.generation.core.processor.ProcessorType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(RichTextController.RICH_TEXT_URI)
public class RichTextController {

    public final static String RICH_TEXT_URI = "api/v1/rich-text";
    private final RichTemplateService richTemplateService;
    private final DocumentProcessorFactory documentProcessorFactory;
    private final ObjectMapper objectMapper;

    public RichTextController(RichTemplateService richTemplateService, DocumentProcessorFactory documentProcessorFactory, ObjectMapper objectMapper) {
        this.richTemplateService = richTemplateService;
        this.documentProcessorFactory = documentProcessorFactory;
        this.objectMapper = objectMapper;
    }


    @PostMapping("/create-template")
    public ResponseEntity<?> uploadFile(@RequestBody RichTemplateRequest request) {
        if (request == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Bad request"));

        try {
            RichTemplate save = richTemplateService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("templateId", save.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/generate/{id}")
    public ResponseEntity<?> generate(@PathVariable Long id) {
        Optional<RichTemplate> richTemplateOptional = richTemplateService.findById(id);
        if (richTemplateOptional.isEmpty())
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Template not found"));

        RichTemplate template = richTemplateOptional.get();

        try {
            JsonNode jsonNode = JsonValidator.parseJson(objectMapper, template.getJson());

            String processedDocument = documentProcessorFactory
                    .getProcessor(ProcessorType.SIMPLE)
                    .process(template.getContent(), jsonNode, RenderType.FREEMARKER);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(processedDocument);
        } catch (RuntimeException e ) {
            e.printStackTrace();
            HashMap<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("cause", String.valueOf(e.getCause()));
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/generate-response/{id}")
    public ResponseEntity<?> generateResponse(@PathVariable Long id) {
        Optional<RichTemplate> richTemplateOptional = richTemplateService.findById(id);
        if (richTemplateOptional.isEmpty())
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Template not found"));

        RichTemplate template = richTemplateOptional.get();

        try {
            JsonNode jsonNode = JsonValidator.parseJson(objectMapper, template.getJson());

            String processedDocument = documentProcessorFactory
                    .getProcessor(ProcessorType.SIMPLE)
                    .process(template.getContent(), jsonNode, RenderType.FREEMARKER);

            return ResponseEntity.status(200).body(Collections.singletonMap("response", processedDocument));
        } catch (RuntimeException e ) {
            e.printStackTrace();
            HashMap<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("cause", String.valueOf(e.getCause()));
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

}
