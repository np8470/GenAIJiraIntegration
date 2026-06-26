package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.client.OllamaClient;
import com.genai.ollamarestapi.exception.AIException;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

        private final OllamaClient ollamaClient;

        private final ObjectMapper objectMapper;
        private final PromptBuilderService promptBuilderService;

        public List<TestCase> generateTestCases(
                        String userStoryDescription, GenerationType type) {

                try {

                        String prompt = promptBuilderService.buildPrompt(
                                        userStoryDescription,
                                        type);

                        String response = ollamaClient.generate(prompt);

                        log.info("AI Response: {}", response);
                        System.out.println("=================================");
                        System.out.println("RAW OLLAMA RESPONSE");
                        System.out.println(response);
                        System.out.println("=================================");

                        String json = extractJson(response);

                        List<TestCase> testCases = objectMapper.readValue(
                                        json,
                                        new TypeReference<List<TestCase>>() {
                                        });
                        log.info("Parsed Test Cases = {}", testCases);

                        return testCases;

                } catch (Exception e) {

                        throw new AIException(
                                        "Failed to generate test cases",
                                        e);
                }
        }

        private String extractJson(String response) {

                int start = response.indexOf("[");
                int end = response.lastIndexOf("]");

                if (start == -1 || end == -1) {
                        throw new RuntimeException(
                                        "No JSON array found in AI response");
                }

                return response.substring(start, end + 1);
        }

        public String buildOutput(List<TestCase> testCases) {

                StringBuilder sb = new StringBuilder();
                log.info("Number of test cases = {}", testCases.size());
                for (TestCase tc : testCases) {
                        log.info("Test Case = {}", tc);
                        sb.append("====================================\n");
                        sb.append("Test Case ID: ")
                                        .append(tc.getId())
                                        .append("\n");

                        sb.append("Title: ")
                                        .append(tc.getTitle())
                                        .append("\n");

                        sb.append("Description: ")
                                        .append(tc.getDescription())
                                        .append("\n");

                        sb.append("Priority: ")
                                        .append(tc.getPriority())
                                        .append("\n");

                        sb.append("Type: ")
                                        .append(tc.getType())
                                        .append("\n");

                        sb.append("Precondition: ")
                                        .append(tc.getPrecondition())
                                        .append("\n");

                        sb.append("Steps:\n");

                        if (tc.getSteps() != null) {
                                int i = 1;
                                for (String step : tc.getSteps()) {
                                        sb.append(i++)
                                                        .append(". ")
                                                        .append(step)
                                                        .append("\n");
                                }
                        }

                        sb.append("Expected Result: ")
                                        .append(tc.getExpectedResult())
                                        .append("\n");

                        sb.append("====================================\n\n");
                }

                return sb.toString();
        }
}