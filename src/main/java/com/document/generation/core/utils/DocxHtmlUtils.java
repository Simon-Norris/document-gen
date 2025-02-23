package com.document.generation.core.utils;

import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DocxHtmlUtils {

    public static String convert(File docxFile) {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docxFile);

            HTMLSettings htmlSettings = new HTMLSettings();
            htmlSettings.setWmlPackage(wordMLPackage);

            ByteArrayOutputStream htmlOutput = new ByteArrayOutputStream();

            // Convert DOCX to HTML using Docx4J
            Docx4J.toHTML(htmlSettings,htmlOutput, 1);

            return htmlOutput.toString(StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can't convert doc file to html");
        }
    }

    public static String convert(InputStream inputStream) {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);

            HTMLSettings htmlSettings = new HTMLSettings();
            htmlSettings.setWmlPackage(wordMLPackage);

            ByteArrayOutputStream htmlOutput = new ByteArrayOutputStream();

            // Convert DOCX to HTML using Docx4J
            Docx4J.toHTML(htmlSettings,htmlOutput, 1);

            return htmlOutput.toString(StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can't convert doc file to html");
        }
    }

    public static byte[] revert(String htmlContent) {
        try {

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

            XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
            wordMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(htmlContent, null));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Docx4J.save(wordMLPackage, byteArrayOutputStream, Docx4J.FLAG_SAVE_ZIP_FILE);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can't convert html file to docx");
        }
    }


    public static void main(String[] args) {
        File docxFile = new File("C:\\Workspace\\Documents\\RD\\DOC-GEN\\WORD\\sample-word.docx");
        String htmlContent = convert(docxFile);
        System.out.println(htmlContent);
        byte[] revert = revert(htmlContent);
        System.out.println(revert.toString());
    }
}
