package com.genai.ollamarestapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.model.BulkRegenerateRequest;
import com.genai.ollamarestapi.model.BulkRegenerateResponse;
import com.genai.ollamarestapi.model.GenerateResponse;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.IndexedTestCaseResult;
import com.genai.ollamarestapi.model.UploadRequest;
import com.genai.ollamarestapi.model.UploadResponse;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.genai.ollamarestapi.model.BulkRegenerateRequest;
import com.genai.ollamarestapi.model.BulkRegenerateResponse;
import com.genai.ollamarestapi.model.IndexedTestCaseRequest;
import com.genai.ollamarestapi.model.IndexedTestCaseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCaseOrchestratorService {

        private final GenerationService generationService;

        private final UploadService uploadService;
        
        private final AiService aiService;

        public GenerateResponse generateOnly(

                        String storyKey,

                        GenerationType type) {

                return generationService.generate(

                                storyKey,

                                type);

        }

        public UploadResponse uploadSelectedToJira(

                        UploadRequest request) {

                return uploadService.upload(

                                request);

        }

        @Audit(action = AuditAction.REGENERATE_TEST_CASE, message = "Bulk regenerated selected test cases")
        public BulkRegenerateResponse regenerateSelected(
                        BulkRegenerateRequest request) {

                List<IndexedTestCaseResult> regeneratedItems = new ArrayList<>();

                if (request == null
                                || request.getItems() == null
                                || request.getItems().isEmpty()) {

                        return BulkRegenerateResponse.builder()
                                        .success(false)
                                        .message("No test cases selected.")
                                        .items(regeneratedItems)
                                        .build();
                }

                for (IndexedTestCaseRequest item : request.getItems()) {

                        long start = System.currentTimeMillis();

                        try {

                                TestCase regenerated = aiService.regenerateTestCase(item.getTestCase());

                                regeneratedItems.add(

                                                IndexedTestCaseResult.builder()

                                                                .index(item.getIndex())

                                                                .clientId(item.getClientId())

                                                                .testCase(regenerated)

                                                                .success(true)

                                                                .message("Success")

                                                                .durationMillis(
                                                                                System.currentTimeMillis() - start)

                                                                .build());

                        } catch (Exception ex) {

                                log.error(
                                                "Failed to regenerate test case {}",
                                                item.getIndex(),
                                                ex);

                                regeneratedItems.add(

                                                IndexedTestCaseResult.builder()

                                                                .index(item.getIndex())

                                                                .clientId(item.getClientId())

                                                                .testCase(item.getTestCase())

                                                                .success(false)

                                                                .message(ex.getMessage())

                                                                .durationMillis(
                                                                                System.currentTimeMillis() - start)

                                                                .build());

                        }

                }

                return BulkRegenerateResponse.builder()

                                .success(true)

                                .message("Bulk regeneration completed.")

                                .items(regeneratedItems)

                                .build();

        }
}