package com.genai.ollamarestapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.exception.AIException;
import com.genai.ollamarestapi.model.GenerateResponse;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.ai.TestCase;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerationService {

    private final JiraDataService jiraDataService;

    private final AiService aiService;

    private final GenerationHistoryService historyService;

    private final UserService userService;

    @Retry(name = "ollamaRetry", fallbackMethod = "ollamaFallback")

    @CircuitBreaker(name = "ollamaCircuit", fallbackMethod = "ollamaFallback")
    public GenerateResponse generate(

            String storyKey,

            GenerationType type) {

        long start = System.currentTimeMillis();

        try {

            JiraDataService.Response story =

                    jiraDataService.apply(

                            new JiraDataService.Request(storyKey));

            List<TestCase> testCases =

                    aiService.generateTestCases(

                            story.acceptanceCriteria(),

                            type);

            long duration =

                    System.currentTimeMillis() - start;

            historyService.saveGeneration(

                    userService.getCurrentUser(),

                    storyKey,

                    story.acceptanceCriteria(),

                    type,

                    testCases,

                    duration);

            GenerateResponse response =

                    new GenerateResponse();

            response.setStoryKey(storyKey);

            response.setGenerationType(type.name());

            response.setGeneratedContent(

                    aiService.buildOutput(testCases));

            response.setTestCases(testCases);

            return response;

        }

        catch (Exception ex) {

            historyService.saveFailure(

                    userService.getCurrentUser(),

                    storyKey,
                    
                    type,

                    System.currentTimeMillis() - start);

            throw ex;

        }

    }

    public GenerateResponse ollamaFallback(

            String storyKey,

            GenerationType type,

            Exception ex) {

        log.error(
                "AI generation failed after retries.",
                ex);

        throw new AIException(

                "AI service is temporarily unavailable. Please try again later.",

                ex);
    }

}