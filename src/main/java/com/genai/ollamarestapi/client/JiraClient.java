package com.genai.ollamarestapi.client;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.genai.ollamarestapi.model.jira.JiraIssueRequest;
import com.genai.ollamarestapi.model.jira.JiraIssueResponse;

@Service
public class JiraClient {

    private final WebClient jiraWebClient;

    public JiraClient(WebClient jiraWebClient) {
        this.jiraWebClient = jiraWebClient;
    }

    public JiraIssueResponse createIssue(JiraIssueRequest request) {

        return jiraWebClient.post()
                .uri("/rest/api/3/issue")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JiraIssueResponse.class)
                .block();
    }

    public void linkIssue(String storyKey, String testKey) {

        Map<String, Object> payload = Map.of(
                "type", Map.of("name", "Relates"),
                "inwardIssue", Map.of("key", storyKey),
                "outwardIssue", Map.of("key", testKey)
        );

        jiraWebClient.post()
                .uri("/rest/api/3/issueLink")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}