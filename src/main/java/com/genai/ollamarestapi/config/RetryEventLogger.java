package com.genai.ollamarestapi.config;

import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RetryEventLogger {

    private final RetryRegistry retryRegistry;

    public RetryEventLogger(RetryRegistry retryRegistry) {
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void init() {

        retryRegistry.getAllRetries().forEach(retry ->

            retry.getEventPublisher()

                .onRetry(event ->

                    System.out.println(
                            "Retry: "
                            + retry.getName()
                            + " Attempt="
                            + event.getNumberOfRetryAttempts()))

                .onSuccess(event ->

                    System.out.println(
                            "Success after retry: "
                            + retry.getName()))

                .onError(event ->

                    System.out.println(
                            "Retry failed: "
                            + retry.getName()))

        );
    }
}