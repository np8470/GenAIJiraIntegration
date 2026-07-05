package com.genai.ollamarestapi.health;

import com.genai.ollamarestapi.model.jira.JiraApiProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JiraHealthIndicator implements HealthIndicator {

    private final WebClient client;

    public JiraHealthIndicator(
            JiraApiProperties props,
            WebClient.Builder builder) {

        this.client = builder

                .baseUrl(props.getApiUrl())

                .defaultHeaders(headers ->
                        headers.setBasicAuth(
                                props.getUsername(),
                                props.getApiToken()))

                .build();
    }

    @Override
    public Health health() {

        try {

            client.get()

                    .uri("/rest/api/2/myself")

                    .retrieve()

                    .bodyToMono(String.class)

                    .block();

            return Health.up()

                    .withDetail("Jira", "Connected")

                    .build();

        } catch (Exception e) {

            return Health.down()

                    .withDetail("Jira", "Unavailable")

                    .withException(e)

                    .build();
        }
    }
}