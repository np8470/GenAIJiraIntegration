package com.genai.ollamarestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.genai.ollamarestapi.model.jira.JiraApiProperties;
import com.genai.ollamarestapi.service.JiraDataService;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
@Configuration
public class JiraFunctionConfig {

    private final JiraApiProperties jiraApiProperties;

    public JiraFunctionConfig(JiraApiProperties jiraApiProperties) {
        this.jiraApiProperties = jiraApiProperties;
    }

    @Bean
    @Description("Provides a function to fetch Jira story details and generate test cases.")
    public Function<JiraDataService.Request, JiraDataService.Response> jiraFunction(JiraDataService jiraDataService)  {
        // Validate Jira configuration properties
        validateJiraConfigProperties(jiraApiProperties);

        // Create and return a new JiraDataService instance
        return jiraDataService;
    }

    private void validateJiraConfigProperties(JiraApiProperties properties) {
       //System.out.println("Jira URL = "+jiraApiProperties.getApiUrl());
        log.info("Jira URL {}", jiraApiProperties.getApiUrl());
        if (properties.getApiUrl() == null || properties.getApiUrl().isEmpty()) {
            throw new IllegalArgumentException("Jira API URL must be provided.");
        }
        if (properties.getApiToken() == null || properties.getApiToken().isEmpty()) {
            throw new IllegalArgumentException("Jira API token must be provided.");
        }
        if (properties.getUsername() == null || properties.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Jira username must be provided.");
        }
    }
}
