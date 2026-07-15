package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.export.common.ReportContext;
import com.genai.ollamarestapi.export.common.ReportContextBuilder;
import com.genai.ollamarestapi.export.excel.ExcelExporter;
import com.genai.ollamarestapi.export.pdf.PdfExporter;
import com.genai.ollamarestapi.export.word.WordExporter;
import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ExcelExporter excelExporter;
     private final PdfExporter pdfExporter;
    /*private final CsvExporter csvExporter;*/
    private final WordExporter wordExporter; 

    public byte[] exportExcel(
            String storyKey,
            List<TestCase> testCases) throws Exception {
        ReportContext context =
            ReportContextBuilder.build(
                    storyKey,
                    testCases);

    return excelExporter.export(context);
    }

    public byte[] exportPdf(
            String storyKey,
            List<TestCase> testCases) throws Exception {
        ReportContext context =
            ReportContextBuilder.build(
                    storyKey,
                    testCases);

    return pdfExporter.export(context);

    }

    /* public byte[] exportCsv(
            String storyKey,
            List<TestCase> testCases) throws Exception {

        return csvExporter.export(storyKey, testCases);
    } */

    public byte[] exportWord(
            String storyKey,
            List<TestCase> testCases) throws Exception {
            ReportContext context =
            ReportContextBuilder.build(
                    storyKey,
                    testCases);
        return wordExporter.export(context);
    } 

}
