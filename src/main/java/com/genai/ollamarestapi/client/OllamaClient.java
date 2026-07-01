package com.genai.ollamarestapi.client;

import com.genai.ollamarestapi.ai.JsonSchemaFactory;
import com.genai.ollamarestapi.model.ai.OllamaRequest;
import com.genai.ollamarestapi.model.ai.OllamaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class OllamaClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Value("${ollama.model:mistral:latest}")
    private String model;

    /**
     * Calls Ollama and returns the complete response.
     */
    public OllamaResponse generate(String prompt) {

        OllamaRequest request = OllamaRequest.builder()
                .model(model)
                .prompt(prompt)
                .stream(false)
                .format(JsonSchemaFactory.testCaseSchema())
                .temperature(0.1)
                .build();

        log.info("==============================================");
        log.info("Calling Ollama");
        log.info("URL      : {}", ollamaUrl);
        log.info("Model    : {}", model);
        log.info("==============================================");

        try {

            OllamaResponse response = webClientBuilder
                    .build()
                    .post()
                    .uri(ollamaUrl + "/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .block();

            if (response == null) {
                throw new RuntimeException("Received null response from Ollama.");
            }

            log.info("==============================================");
            log.info("Ollama Response");
            log.info("Model            : {}", response.getModel());
            log.info("Done             : {}", response.isDone());
            log.info("Done Reason      : {}", response.getDoneReason());
            log.info("Prompt Tokens    : {}", response.getPromptEvalCount());
            log.info("Generated Tokens : {}", response.getEvalCount());
            log.info("==============================================");

            if (response.getResponse() == null ||
                    response.getResponse().isBlank()) {

                throw new RuntimeException(
                        "Ollama returned an empty response.");
            }

            log.info("Raw AI Response:\n{}", response.getResponse());

            return response;

        } catch (Exception ex) {

            log.error("Error while calling Ollama.", ex);

            throw new RuntimeException(
                    "Failed to call Ollama API.",
                    ex);
        }
    }
}