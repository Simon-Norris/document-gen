package com.document.generation.core.processor;

import com.document.generation.core.RenderType;
import com.fasterxml.jackson.databind.JsonNode;

public interface DocumentProcessor {

    <T, R> R process(T template, JsonNode jsonNode, RenderType renderType);
}
