package com.document.generation.core.templateValidation;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FreemarkerValidator implements TemplateValidator {

    @Override
    public Set<String> extractTemplateKeys(String templateContent) {
        Set<String> keys = new HashSet<>();
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(templateContent);
        while (matcher.find()) {
            keys.add(matcher.group(1).trim());
        }
        return keys;
    }

    @Override
    public TemplateValidationResult validateTemplateKeys(Map<String, Object> jsonData, Set<String> templateKeys) {
        for (String key : templateKeys) {
            if (!jsonData.containsKey(key)) {
                Set<String> keyName = new HashSet<>();
                keyName.add(key);
                return new TemplateValidationResult(false, keyName);
            }
        }
        return new TemplateValidationResult(true, templateKeys);
    }
}
