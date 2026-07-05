package com.genai.ollamarestapi.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AIHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        return Health.up()

                .withDetail("Model", "Mistral")

                .withDetail("Status", "Ready")

                .build();
    }
}