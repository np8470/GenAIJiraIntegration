package com.genai.ollamarestapi.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OllamaHealthIndicator implements HealthIndicator {

    private final WebClient webClient;

    public OllamaHealthIndicator(WebClient.Builder builder) {

        this.webClient = builder

                .baseUrl("http://localhost:11434")

                .build();
    }

    @Override
    public Health health() {

        try {

            webClient.get()

                    .uri("/api/tags")

                    .retrieve()

                    .bodyToMono(String.class)

                    .block();

            return Health.up()

                    .withDetail("Ollama", "Running")

                    .build();

        } catch (Exception e) {

            return Health.down()

                    .withDetail("Ollama", "Unavailable")

                    .withException(e)

                    .build();
        }
    }
}