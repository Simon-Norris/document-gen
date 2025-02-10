package com.document.generation.core;

public interface DocumentRenderer {
    <T, R> R render(T template, Object... args);
}
