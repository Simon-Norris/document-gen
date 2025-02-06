package com.document.generation.app.entity;

import jakarta.persistence.*;

@Entity
public class DocumentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String templateName;
    private String templateType;

    @Lob
    private byte[] templateContent;

    private String jsonFileName;
    private String jsonFileType;
    @Lob
    private byte[] jsonFileContent;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public byte[] getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(byte[] templateContent) {
        this.templateContent = templateContent;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public String getJsonFileType() {
        return jsonFileType;
    }

    public void setJsonFileType(String jsonFileType) {
        this.jsonFileType = jsonFileType;
    }

    public byte[] getJsonFileContent() {
        return jsonFileContent;
    }

    public void setJsonFileContent(byte[] jsonFileContent) {
        this.jsonFileContent = jsonFileContent;
    }

}
