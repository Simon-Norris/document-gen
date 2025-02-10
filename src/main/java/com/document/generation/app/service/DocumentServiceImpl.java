package com.document.generation.app.service;

import com.document.generation.app.entity.DocumentFile;
import com.document.generation.app.repo.DocumentFileRepository;
import com.document.generation.app.utils.JsonValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentFileRepository documentFileRepository;

    public DocumentServiceImpl(DocumentFileRepository documentFileRepository) {
        this.documentFileRepository = documentFileRepository;
    }

    /**
     * Uploads and stores a document in the database.
     */
    @Override
    public DocumentFile uploadDocument(MultipartFile templateFile, MultipartFile jsonFile) throws Exception {
        DocumentFile doc = new DocumentFile();
        doc.setTemplateName(templateFile.getOriginalFilename());
        doc.setTemplateType(templateFile.getContentType());
        doc.setTemplateContent(templateFile.getBytes());

        doc.setJsonFileName(jsonFile.getOriginalFilename());
        doc.setJsonFileType(jsonFile.getContentType());
        doc.setJsonFileContent(jsonFile.getBytes());

        return documentFileRepository.save(doc);
    }

    @Override
    public Optional<DocumentFile> findById(Long id) {
        return documentFileRepository.findById(id);
    }
}
