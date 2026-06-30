package com.genai.ollamarestapi.client;

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

    public String generate(String prompt) {

        OllamaRequest request = new OllamaRequest(
                model,
                prompt,
                false,
                "json");

        log.info("Calling Ollama using model {}", model);

        OllamaResponse response = webClientBuilder.build()
                .post()
                .uri(ollamaUrl + "/api/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .block();

        if (response == null || response.getResponse() == null) {
            throw new RuntimeException("Empty response received from Ollama.");
        }

        log.info("Raw AI Response:\n{}", response.getResponse());

        return response.getResponse();
    }
}