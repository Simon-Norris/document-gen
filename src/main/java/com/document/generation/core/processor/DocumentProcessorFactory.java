package com.document.generation.core.processor;

import com.document.generation.core.*;
import org.springframework.stereotype.Component;

@Component
public class DocumentProcessorFactory {

    private final DocumentRendererFactory rendererFactory;

    public DocumentProcessorFactory(DocumentRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }


    public DocumentProcessor getProcessor(ProcessorType type) {
        return switch (type) {
            case SIMPLE -> new SimpleProcessor(rendererFactory);
            case WORD -> new WordProcessor(rendererFactory);
            case HTML -> new HtmlProcessor(rendererFactory);
            case FTL -> new FtlProcessor(rendererFactory);
            default -> throw new IllegalArgumentException("Unsupported processor: " + type);
        };
    }
}
