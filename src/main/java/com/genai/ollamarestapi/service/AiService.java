package com.genai.ollamarestapi.service;

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

    /**
     * Generate Manual/API/Selenium Test Cases using Ollama.
     */
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
            List<TestCase> testCases =
                    responseParser.parse(response.getResponse());

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
}