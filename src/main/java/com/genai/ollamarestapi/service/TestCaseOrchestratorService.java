package com.genai.ollamarestapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.exception.AIException;
import com.genai.ollamarestapi.exception.JiraException;
import com.genai.ollamarestapi.model.GenerateResponse;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.UploadResponse;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.genai.ollamarestapi.model.jira.JiraApiProperties;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestCaseOrchestratorService {

        private final JiraDataService jiraDataService;
        private final AiService aiService;
        private final JiraService jiraService;
        private final JiraApiProperties jiraApiProperties;

        @Retry(name = "ollamaRetry", fallbackMethod = "ollamaFallback")
        @CircuitBreaker(name = "ollamaCircuit", fallbackMethod = "ollamaFallback")
        @Audit(action = AuditAction.GENERATE_RESPONSE, message = "Generate response from Work Item id {0} for Generation type {1}")
        public GenerateResponse generateOnly(
                        String storyKey,
                        GenerationType type,
                        HttpSession session) {
                System.out.println("Inside generateOnly()");
                try {

                        JiraDataService.Response story = jiraDataService.apply(
                                        new JiraDataService.Request(storyKey));

                        List<TestCase> testCases = aiService.generateTestCases(
                                        story.acceptanceCriteria(),
                                        type);

                        session.setAttribute("storyKey", storyKey);
                        session.setAttribute("generationType", type);
                        session.setAttribute("generatedTestCases", testCases);

                        String output = aiService.buildOutput(testCases);

                        GenerateResponse response = new GenerateResponse();

                        response.setStoryKey(storyKey);
                        response.setGenerationType(type.name());
                        response.setGeneratedContent(output);
                        response.setTestCases(testCases);

                        return response;
                } catch (WebClientResponseException.NotFound ex) {
                        log.warn("Issue not found: {}", storyKey);
                        throw new JiraException("Issue not found: " + storyKey);
                }
        }

        @Audit(action = AuditAction.UPLOAD_SELECTED_TO_JIRA, message = "Upload number of selected testcases {0} to Jira")
        @SuppressWarnings("unchecked")
        public UploadResponse uploadSelectedToJira(
                        List<Integer> selectedIndexes,
                        HttpSession session) {

                String storyKey = (String) session.getAttribute("storyKey");

                List<TestCase> testCases = (List<TestCase>) session.getAttribute("generatedTestCases");

                if (storyKey == null || testCases == null) {

                        return new UploadResponse(
                                        false,
                                        "Please generate test cases first.",
                                        0,
                                        0,
                                        List.of());
                }

                int uploadedCount = 0;
                int failedCount = 0;

                List<String> jiraLinks = new ArrayList<>();

                for (Integer index : selectedIndexes) {

                        try {

                                TestCase tc = testCases.get(index);

                                String testCaseKey = jiraService.createTestCase(
                                                "SCRUM",
                                                tc);

                                jiraService.linkIssue(
                                                storyKey,
                                                testCaseKey);

                                uploadedCount++;

                                String jiraLink = "https://genaiauto.atlassian.net/browse/" + testCaseKey;

                                jiraLinks.add(jiraLink);

                                log.info("Uploaded Test Case : {}", testCaseKey);

                        } catch (Exception ex) {

                                failedCount++;

                                log.error(
                                                "Failed to upload test case : {}",
                                                testCases.get(index).getTitle(),
                                                ex);
                        }

                }

                boolean success = failedCount == 0;

                String message = uploadedCount + " of "
                                + selectedIndexes.size()
                                + " test case(s) uploaded successfully.";

                return new UploadResponse(
                                success,
                                message,
                                uploadedCount,
                                failedCount,
                                jiraLinks);
        }

        public GenerateResponse ollamaFallback(
                        String storyKey,
                        GenerationType type,
                        HttpSession session,
                        Exception ex) {

                throw new AIException(
                                "AI service is temporarily unavailable. Please try again later.",
                                ex);
        }

        private String buildJiraDescription(TestCase tc) {

                StringBuilder sb = new StringBuilder();

                sb.append("Description\n");
                sb.append("-------------------------\n");
                sb.append(nullToEmpty(tc.getDescription()));

                sb.append("\n\nPriority\n");
                sb.append("-------------------------\n");
                sb.append(nullToEmpty(tc.getPriority()));

                sb.append("\n\nType\n");
                sb.append("-------------------------\n");
                sb.append(nullToEmpty(tc.getType()));

                sb.append("\n\nPrecondition\n");
                sb.append("-------------------------\n");
                sb.append(nullToEmpty(tc.getPrecondition()));

                sb.append("\n\nSteps\n");
                sb.append("-------------------------\n");

                if (tc.getSteps() != null) {

                        int i = 1;

                        for (String step : tc.getSteps()) {

                                sb.append(i++)
                                                .append(". ")
                                                .append(step)
                                                .append("\n");
                        }
                }

                sb.append("\nExpected Result\n");
                sb.append("-------------------------\n");
                sb.append(nullToEmpty(tc.getExpectedResult()));

                return sb.toString();
        }

        private String nullToEmpty(String value) {
                return value == null ? "" : value;
        }

        public String generateAndCreate(String storyKey, GenerationType type) {

                JiraDataService.Response story = jiraDataService.apply(
                                new JiraDataService.Request(storyKey));

                List<TestCase> testCases = aiService.generateTestCases(
                                story.acceptanceCriteria(), type);

                for (TestCase tc : testCases) {
                        log.info("Parsed Test Case: {}", tc);

                        System.out.println("Steps = " + tc.getSteps());
                        System.out.println("Expected = " + tc.getExpectedResult());
                        String testCaseKey = jiraService.createTestCase(
                                        "SCRUM",
                                        tc);

                        jiraService.linkIssue(
                                        storyKey,
                                        testCaseKey);
                }

                String finalOutput = aiService.buildOutput(testCases);

                log.info("Final Output:\n{}", finalOutput);

                return finalOutput;
        }

}