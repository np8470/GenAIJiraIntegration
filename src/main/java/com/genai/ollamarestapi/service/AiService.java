package com.genai.ollamarestapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genai.ollamarestapi.client.OllamaClient;
import com.genai.ollamarestapi.exception.AIException;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final OllamaClient ollamaClient;
    private final ObjectMapper objectMapper;
    private final PromptBuilderService promptBuilderService;

    public List<TestCase> generateTestCases(
            String userStoryDescription,
            GenerationType type) {

        try {

            String prompt = promptBuilderService.buildPrompt(
                    userStoryDescription,
                    type);

            String response = ollamaClient.generate(prompt);

            log.info("AI Response:\n{}", response);

            return parseResponse(response);

        } catch (Exception e) {

            log.error("AI Parsing Failed", e);

            throw new AIException(
                    "Failed to generate test cases",
                    e);
        }
    }

    /**
     * Parses every response format returned by Ollama.
     */
    private List<TestCase> parseResponse(String response) throws Exception {

        String json = extractJson(response);

        JsonNode root = objectMapper.readTree(json);

        if (root.isArray()) {

            return objectMapper.readValue(
                    json,
                    new TypeReference<List<TestCase>>() {
                    });
        }

        if (root.isObject()) {

            TestCase tc = objectMapper.treeToValue(
                    root,
                    TestCase.class);

            return Collections.singletonList(tc);
        }

        throw new RuntimeException("Unsupported AI response.");
    }

    /**
     * Extract JSON object or array from AI response.
     */
    private String extractJson(String response) {

        response = response.trim();

        // remove markdown if present
        response = response.replace("```json", "");
        response = response.replace("```", "").trim();

        int objStart = response.indexOf("{");
        int arrStart = response.indexOf("[");

        if (objStart == -1 && arrStart == -1) {
            throw new RuntimeException("No JSON found.");
        }

        if (arrStart != -1 &&
                (objStart == -1 || arrStart < objStart)) {

            int arrEnd = response.lastIndexOf("]");

            return response.substring(arrStart, arrEnd + 1);
        }

        int objEnd = response.lastIndexOf("}");

        return response.substring(objStart, objEnd + 1);
    }

    public String buildOutput(List<TestCase> testCases) {

        StringBuilder sb = new StringBuilder();

        for (TestCase tc : testCases) {

            sb.append("====================================\n");
            sb.append("Test Case ID: ").append(tc.getId()).append("\n");
            sb.append("Title: ").append(tc.getTitle()).append("\n");
            sb.append("Description: ").append(tc.getDescription()).append("\n");
            sb.append("Priority: ").append(tc.getPriority()).append("\n");
            sb.append("Type: ").append(tc.getType()).append("\n");
            sb.append("Precondition: ").append(tc.getPrecondition()).append("\n");

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