package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.client.OllamaClient;
import com.genai.ollamarestapi.exception.AIException;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.ai.OllamaResponse;
import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

        private final OllamaClient ollamaClient;

        private final PromptBuilderService promptBuilderService;

        private final ResponseParser responseParser;

        private final TestCaseNormalizerService normalizer;

        /**
         * Generate Manual/API/Selenium Test Cases using Ollama.
         */
        @Audit(action = AuditAction.GENERATE_TEST_CASE, message = "Generated Test Cases from User Story description for generation type {1}")
        public List<TestCase> generateTestCases(
                        String userStoryDescription,
                        GenerationType type) {

                try {

                        // Build AI Prompt
                        String prompt = promptBuilderService.buildPrompt(
                                        userStoryDescription,
                                        type);

                        log.info("Calling Ollama...");

                        // Call Ollama
                        OllamaResponse response = ollamaClient.generate(prompt);

                        if (response == null) {
                                throw new RuntimeException("Received null response from Ollama.");
                        }

                        log.info("========================================");
                        log.info("Model              : {}", response.getModel());
                        log.info("Done               : {}", response.isDone());
                        log.info("Prompt Tokens      : {}", response.getPromptEvalCount());
                        log.info("Generated Tokens   : {}", response.getEvalCount());
                        log.info("========================================");

                        log.info("Raw AI Response:\n{}", response.getResponse());

                        // Parse JSON returned by AI
                        List<TestCase> testCases = responseParser.parse(response.getResponse());

                        normalizer.normalizeGenerated(testCases);

                        log.info("Successfully parsed {} test cases.",
                                        testCases.size());

                        return testCases;

                } catch (Exception ex) {

                        log.error("Failed while generating test cases.", ex);

                        throw new AIException(
                                        "Failed to generate test cases.",
                                        ex);
                }
        }

        /**
         * Converts test cases to readable text.
         */
        @Audit(action = AuditAction.FORMAT_TEST_CASE, message = "Converts test cases to readable text ")
        public String buildOutput(List<TestCase> testCases) {

                StringBuilder sb = new StringBuilder();

                for (TestCase tc : testCases) {

                        sb.append("====================================\n");

                        sb.append("Test Case ID : ")
                                        .append(tc.getId())
                                        .append("\n");

                        sb.append("Title : ")
                                        .append(tc.getTitle())
                                        .append("\n");

                        sb.append("Description : ")
                                        .append(tc.getDescription())
                                        .append("\n");

                        sb.append("Priority : ")
                                        .append(tc.getPriority())
                                        .append("\n");

                        sb.append("Type : ")
                                        .append(tc.getType())
                                        .append("\n");

                        sb.append("Precondition : ")
                                        .append(tc.getPrecondition())
                                        .append("\n");

                        sb.append("Steps:\n");

                        if (tc.getSteps() != null) {

                                int stepNo = 1;

                                for (String step : tc.getSteps()) {

                                        sb.append(stepNo++)
                                                        .append(". ")
                                                        .append(step)
                                                        .append("\n");
                                }
                        }

                        sb.append("Expected Result : ")
                                        .append(tc.getExpectedResult())
                                        .append("\n");

                        sb.append("====================================\n\n");
                }

                return sb.toString();
        }

        /*
         * @Audit(action = AuditAction.REGENERATE_TEST_CASE, message =
         * "Regenerated AI Test Case {0}")
         * public TestCase regenerateTestCase(TestCase testCase) {
         * 
         * try {
         * 
         * String prompt = promptBuilderService
         * .buildRegeneratePrompt(testCase);
         * 
         * log.info("Regenerating Test Case using Ollama...");
         * 
         * OllamaResponse response = ollamaClient.generate(prompt);
         * 
         * if (response == null) {
         * 
         * throw new RuntimeException(
         * "Received null response from Ollama.");
         * 
         * }
         * 
         * List<TestCase> regenerated = responseParser.parse(response.getResponse());
         * 
         * if (regenerated.isEmpty()) {
         * 
         * throw new RuntimeException(
         * "No regenerated test case returned.");
         * 
         * }
         * 
         * TestCase tc = regenerated.get(0);
         * 
         * tc.setClientId(testCase.getClientId());
         * 
         * tc.setId(testCase.getId());
         * 
         * if (tc.getPriority() == null || tc.getPriority().isBlank()) {
         * 
         * tc.setPriority("Medium");
         * 
         * }
         * 
         * if (tc.getType() == null || tc.getType().isBlank()) {
         * 
         * tc.setType("Functional");
         * 
         * }
         * 
         * if (tc.getDescription() == null || tc.getDescription().isBlank()) {
         * 
         * tc.setDescription(tc.getTitle());
         * 
         * }
         * 
         * return tc;
         * 
         * } catch (Exception ex) {
         * 
         * log.error("Failed to regenerate test case.", ex);
         * 
         * throw new AIException(
         * "Failed to regenerate test case.",
         * ex);
         * 
         * }
         * 
         * }
         */

        /**
         * Bulk regenerate selected test cases.
         */

        public List<TestCase> regenerateTestCases(
                        List<TestCase> testCases) {

                return testCases
                                .parallelStream()
                                .map(this::regenerateTestCase)
                                .toList();

        }

        /*
         * public TestCase regenerateTestCase(TestCase original) {
         * 
         * String prompt = promptBuilderService
         * .buildRegeneratePrompt(original);
         * 
         * OllamaResponse response = ollamaClient.generate(prompt);
         * 
         * TestCase regenerated = responseParser
         * .parse(response.getResponse())
         * .get(0);
         * 
         * 
         * 
         * regenerated.setClientId(original.getClientId());
         * 
         * regenerated.setId(original.getId());
         * 
         * return regenerated;
         * 
         * }
         */

        @Audit(action = AuditAction.REGENERATE_TEST_CASE, message = "Regenerated AI Test Case {0}")
        public TestCase regenerateTestCase(TestCase original) {

                try {

                        String prompt = promptBuilderService.buildRegeneratePrompt(original);

                        log.info("Regenerating Test Case...");

                        OllamaResponse response = ollamaClient.generate(prompt);

                        if (response == null) {

                                throw new RuntimeException(
                                                "Received null response from Ollama.");

                        }

                        List<TestCase> regeneratedList = responseParser.parse(response.getResponse());

                        if (regeneratedList.isEmpty()) {

                                throw new RuntimeException(
                                                "No regenerated test case returned.");

                        }

                        TestCase regenerated = regeneratedList.get(0);

                        return normalizer.normalizeRegenerated(
                                        original,
                                        regenerated);

                }

                catch (Exception ex) {

                        log.error("Failed to regenerate test case.", ex);

                        throw new AIException(
                                        "Failed to regenerate test case.",
                                        ex);

                }
        }
}