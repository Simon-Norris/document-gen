package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.entity.RichTemplate;
import com.document.generation.app.utils.JsonValidator;
import com.document.generation.core.RenderType;
import com.document.generation.core.processor.DocumentProcessorFactory;
import com.document.generation.core.utils.DocxHtmlUtils;
import com.document.generation.core.processor.ProcessorType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class GenerationService {

    private final ObjectMapper objectMapper;
    private final DocumentProcessorFactory documentProcessorFactory;

    public GenerationService(ObjectMapper objectMapper, DocumentProcessorFactory documentProcessorFactory) {
        this.objectMapper = objectMapper;
        this.documentProcessorFactory = documentProcessorFactory;
    }

    public  <T> byte[] generateDocument(T entity, String docType) {
        if (entity instanceof DocumentFile documentFile) {
            JsonNode jsonNode = JsonValidator.parseJson(objectMapper, documentFile.getJsonFileContent());

            boolean isWordDoc = docType.equals("docx");
            boolean isHtmlDoc = docType.equals("html");
            boolean isFtlDoc = docType.equals("ftl");

            if (isWordDoc) {
                return documentProcessorFactory
                        .getProcessor(ProcessorType.WORD)
                        .process(documentFile.getTemplateContent(), jsonNode, RenderType.FREEMARKER);
            } else if (isHtmlDoc) {
                return documentProcessorFactory
                        .getProcessor(ProcessorType.HTML)
                        .process(documentFile.getTemplateContent(), jsonNode, RenderType.FREEMARKER);
            } else if (isFtlDoc) {
                return documentProcessorFactory
                        .getProcessor(ProcessorType.FTL)
                        .process(documentFile.getTemplateContent(), jsonNode, RenderType.FREEMARKER);
            } else {
                throw new IllegalArgumentException("Document type not identified");
            }
        } else if (entity instanceof RichTemplate richTemplate) {
            JsonNode jsonNode = JsonValidator.parseJson(objectMapper, richTemplate.getJson());
            return documentProcessorFactory.getProcessor(ProcessorType.WORD).process(richTemplate.getContent(), jsonNode, RenderType.FREEMARKER);
        } else {
            throw new IllegalArgumentException("Entity not found");
        }
    }
}
