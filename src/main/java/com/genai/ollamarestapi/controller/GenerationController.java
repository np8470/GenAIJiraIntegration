package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.model.GenerateResponse;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.UploadRequest;
import com.genai.ollamarestapi.model.UploadResponse;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.genai.ollamarestapi.service.AiService;
import com.genai.ollamarestapi.service.TestCaseOrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.genai.ollamarestapi.model.BulkRegenerateRequest;
import com.genai.ollamarestapi.model.BulkRegenerateResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GenerationController {

    private final TestCaseOrchestratorService service;
    private final AiService aiService;

    @GetMapping("/generate")
    @Audit(action = AuditAction.GENERATE_TEST_CASE, message = "Generated AI Test Cases for Work Item {0} using type {1}")
    public GenerateResponse generate(

            @RequestParam String storyKey,
            @RequestParam String type) {

        log.info("Story Key {}", storyKey);
        log.info("Type {}", type);

        return service.generateOnly(
                storyKey,
                GenerationType.valueOf(type));
    }

    @PostMapping("/upload")
    @Audit(action = AuditAction.UPLOAD_TO_JIRA, message = "Uploaded Test Cases to Jira")
    public UploadResponse uploadToJira(
            @RequestBody UploadRequest request) {

        log.info("Uploading {} test cases's Story key", request.getStoryKey());
        log.info("Uploading {} test cases list", request.getTestCaseIds());

        return service.uploadSelectedToJira(request);
    }

    @PostMapping("/regenerate")
    public TestCase regenerate(
            @RequestBody TestCase testCase) {

        return aiService.regenerateTestCase(testCase);

    }

    @PostMapping("/regenerate-selected")
    public BulkRegenerateResponse regenerateSelected(
            @RequestBody BulkRegenerateRequest request) {
        return service.regenerateSelected(request);
    }
}