package com.document.generation.core.templateValidation;

import java.util.Map;
import java.util.Set;

public interface TemplateValidator {
    TemplateValidationResult validateTemplateKeys(Map<String, Object> jsonData, Set<String> templateKeys);
    Set<String> extractTemplateKeys(String templateContent);
}
