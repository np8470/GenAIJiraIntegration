package com.genai.ollamarestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.genai.ollamarestapi.model.jira.JiraApiProperties;

import java.util.Base64;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient jiraWebClient(JiraApiProperties props) {

        String auth = Base64.getEncoder()
                .encodeToString((props.getUsername() + ":" + props.getApiToken()).getBytes());

        return WebClient.builder()
                .baseUrl(props.getApiUrl())
                .defaultHeader("Authorization", "Basic " + auth)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}