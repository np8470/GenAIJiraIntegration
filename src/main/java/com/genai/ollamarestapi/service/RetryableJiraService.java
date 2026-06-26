package com.genai.ollamarestapi.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.client.JiraClient;
import com.genai.ollamarestapi.model.jira.JiraIssueRequest;
import com.genai.ollamarestapi.model.jira.JiraIssueResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetryableJiraService {

    private final JiraClient jiraClient;

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000))
    public JiraIssueResponse createIssue(
            JiraIssueRequest request) {

        return jiraClient.createIssue(request);
    }
}
