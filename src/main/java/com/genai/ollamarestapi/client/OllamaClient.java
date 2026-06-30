package com.genai.ollamarestapi.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OllamaClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Value("${ollama.model:mistral}")
    private String model;

    public String generate(String prompt) {

        Map<String, Object> request = Map.of(
                "model", model,
                "prompt", prompt,
                "stream", false);

        log.info("Ollama URL : {}", ollamaUrl);
        log.info("Model      : {}", model);

        try {

            Map<?, ?> response = webClientBuilder.build()
                    .post()
                    .uri(ollamaUrl + "/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Ollama Response : {}", response);

            return String.valueOf(response.get("response"));

        } catch (Exception ex) {

            log.error("Ollama Error", ex);

            throw ex;
        }
    }
}