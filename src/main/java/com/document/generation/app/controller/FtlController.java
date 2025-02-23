package com.document.generation.app.controller;

import com.document.generation.app.utils.JsonValidator;
import com.document.generation.core.RenderType;
import com.document.generation.core.processor.DocumentProcessorFactory;
import com.document.generation.core.processor.ProcessorType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ftl")
public class FtlController {

    private final DocumentProcessorFactory documentProcessorFactory;
    private final ObjectMapper objectMapper;

    public FtlController(DocumentProcessorFactory documentProcessorFactory, ObjectMapper objectMapper) {
        this.documentProcessorFactory = documentProcessorFactory;
        this.objectMapper = objectMapper;
    }


    @PostMapping("/render")
    public ResponseEntity<?> renderTemplate(@RequestBody Map<String, String> request) {
        String templateContent = request.get("templateContent");
        String values = request.get("values");

        JsonNode jsonNode = JsonValidator.parseJson(objectMapper, values);

        String process = documentProcessorFactory
                .getProcessor(ProcessorType.SIMPLE)
                .process(templateContent, jsonNode, RenderType.FREEMARKER);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(Collections.singletonMap("response", process));

    }
}
