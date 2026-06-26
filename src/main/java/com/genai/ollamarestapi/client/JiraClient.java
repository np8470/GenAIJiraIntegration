package com.genai.ollamarestapi.client;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import com.genai.ollamarestapi.model.jira.JiraIssueRequest;
import com.genai.ollamarestapi.model.jira.JiraIssueResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JiraClient {

    private final WebClient webClient;

    @Value("${jira.apiUrl}")
    private String jiraUrl;

    @Value("${jira.username}")
    private String username;

    @Value("${jira.apiToken}")
    private String token;

    private String authHeader() {
        return Base64.getEncoder()
                .encodeToString((username + ":" + token).getBytes());
    }

    public JiraIssueResponse createIssue(JiraIssueRequest request) {

        return webClient.post()
                .uri(jiraUrl + "/rest/api/3/issue")
                .header("Authorization", "Basic " + authHeader())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JiraIssueResponse.class)
                .block();
    } 



    /* public JiraIssueResponse createIssue(
            JiraIssueRequest request) {

        return jiraWebClient.post()
                .uri("/issue")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JiraIssueResponse.class)
                .block();
    } */


    public void linkIssue(String storyKey, String testKey) {

        Map<String, Object> payload = Map.of(
                "type", Map.of("name", "Relates"),
                "inwardIssue", Map.of("key", storyKey),
                "outwardIssue", Map.of("key", testKey)
        );

        webClient.post()
                .uri(jiraUrl + "/rest/api/3/issueLink")
                .header("Authorization", "Basic " + authHeader())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
