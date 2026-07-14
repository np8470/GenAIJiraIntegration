package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.export.ExcelExporter;
import com.genai.ollamarestapi.export.PdfExporter;
import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ExcelExporter excelExporter;
     private final PdfExporter pdfExporter;
    /*private final CsvExporter csvExporter;
    private final WordExporter wordExporter; */

    public byte[] exportExcel(
            String storyKey,
            List<TestCase> testCases) throws Exception {

        return excelExporter.export(storyKey, testCases);
    }

    public byte[] exportPdf(
            String storyKey,
            List<TestCase> testCases) throws Exception {

        return pdfExporter.export(storyKey, testCases);
    }

    /* public byte[] exportCsv(
            String storyKey,
            List<TestCase> testCases) throws Exception {

        return csvExporter.export(storyKey, testCases);
    }

    public byte[] exportWord(
            String storyKey,
            List<TestCase> testCases) throws Exception {

        return wordExporter.export(storyKey, testCases);
    } */

}
