package com.document.generation.core.templateValidation;

import com.document.generation.core.RenderType;

public class TemplateValidatorFactory {

    public static TemplateValidator getValidator(RenderType renderType) {
        switch (renderType) {
            case FREEMARKER:
                return new FreemarkerValidator();
            case MUSTACHE:
                return new MustacheValidator();
            default:
                throw new IllegalArgumentException("Unsupported template engine type: " + renderType);
        }
    }
}
