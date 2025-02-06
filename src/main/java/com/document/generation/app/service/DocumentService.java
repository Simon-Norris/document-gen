package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface DocumentService {

    DocumentFile uploadDocument(MultipartFile templateFile, MultipartFile jsonFile) throws Exception;

    Optional<DocumentFile> findById(Long id);
}
