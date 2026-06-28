package com.genai.ollamarestapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.genai.ollamarestapi.exception.JiraException;
import com.genai.ollamarestapi.model.GenerateResponse;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.ai.TestCase;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestCaseOrchestratorService {

        private final JiraDataService jiraDataService;
        private final AiService aiService;
        private final JiraService jiraService;

        public GenerateResponse generateOnly(
                        String storyKey,
                        GenerationType type,
                        HttpSession session) {

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

        @SuppressWarnings("unchecked")
        public String uploadSelectedToJira(
                        List<Integer> selectedIndexes,
                        HttpSession session) {

                String storyKey = (String) session.getAttribute("storyKey");

                List<TestCase> testCases = (List<TestCase>) session.getAttribute("generatedTestCases");

                if (storyKey == null || testCases == null) {
                        return "Please generate test cases first.";
                }

                for (Integer index : selectedIndexes) {

                        TestCase tc = testCases.get(index);

                        String jiraDescription =
                                        // aiService.buildDescription(tc);
                                        jiraService.buildJiraDescription(tc);

                        String testCaseKey = jiraService.createTestCase(
                                        "SCRUM",
                                        tc);

                        jiraService.linkIssue(
                                        storyKey,
                                        testCaseKey);
                }

                return "Selected test cases uploaded successfully.";
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