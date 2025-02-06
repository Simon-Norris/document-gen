package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface DocumentService {

    byte[] generateWord(Map<String, Object> model) throws Exception;
    DocumentFile uploadDocument(MultipartFile file) throws Exception;
}
