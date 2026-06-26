package com.genai.ollamarestapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

                JiraDataService.Response story = jiraDataService.apply(
                                new JiraDataService.Request(storyKey));

                List<TestCase> testCases = aiService.generateTestCases(
                                story.acceptanceCriteria(),
                                type);

                session.setAttribute(
                                "storyKey",
                                storyKey);

                session.setAttribute(
                                "testCases",
                                testCases);

                String output = aiService.buildOutput(testCases);

                GenerateResponse response = new GenerateResponse();

                response.setStoryKey(storyKey);
                response.setGenerationType(type.name());
                response.setGeneratedContent(output);

                return response;
        }

        @SuppressWarnings("unchecked")
        public String uploadToJira(
                        HttpSession session) {

                String storyKey = (String) session.getAttribute("storyKey");

                List<TestCase> testCases = (List<TestCase>) session.getAttribute("testCases");

                if (storyKey == null || testCases == null) {
                        return "Please generate test cases first.";
                }

                for (TestCase tc : testCases) {

                        String description = jiraService.buildDescription(tc);

                        String key = jiraService.createTestCase(
                                        "SCRUM",
                                        tc);

                        jiraService.linkIssue(
                                        storyKey,
                                        key);
                }

                session.removeAttribute("storyKey");
                session.removeAttribute("testCases");

                return testCases.size() +
                                " test cases uploaded successfully.";
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