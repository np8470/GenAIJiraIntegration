package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.model.ExportRequest;
import com.genai.ollamarestapi.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @PostMapping("/excel")
    public ResponseEntity<byte[]> excel(
            @RequestBody ExportRequest request) throws Exception {

        byte[] file =
                exportService.exportExcel(
                        request.getStoryKey(),
                        request.getTestCases());

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=TestCases.xlsx")

                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))

                .body(file);

    }

    /* @PostMapping("/csv")
    public ResponseEntity<byte[]> csv(
            @RequestBody ExportRequest request) throws Exception {

        byte[] file =
                exportService.exportCsv(
                        request.getStoryKey(),
                        request.getTestCases());

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=TestCases.csv")

                .contentType(MediaType.TEXT_PLAIN)

                .body(file);

    } */

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> pdf(
            @RequestBody ExportRequest request) throws Exception {

        byte[] file =
                exportService.exportPdf(
                        request.getStoryKey(),
                        request.getTestCases());

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=TestCases.pdf")

                .contentType(MediaType.APPLICATION_PDF)

                .body(file);

    }

    @PostMapping("/word")
    public ResponseEntity<byte[]> word(
            @RequestBody ExportRequest request) throws Exception {

        byte[] file =
                exportService.exportWord(
                        request.getStoryKey(),
                        request.getTestCases());

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=TestCases.docx")

                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))

                .body(file);

    }

}