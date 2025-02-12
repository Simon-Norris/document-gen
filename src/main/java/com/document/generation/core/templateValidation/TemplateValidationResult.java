package com.document.generation.core.templateValidation;

import java.util.Set;

public record TemplateValidationResult(
        boolean status,

        Set<String> templateKeys
) { }
