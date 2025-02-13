package com.document.generation.app.dto;

public record RichTemplateRequest(
        Long id,
        String content,
        String name,
        String json

) {
}
