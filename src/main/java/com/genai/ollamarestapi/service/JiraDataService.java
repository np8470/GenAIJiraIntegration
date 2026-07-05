package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.model.jira.JiraApiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.function.Function;

@Validated
@Service
public class JiraDataService implements Function<JiraDataService.Request, JiraDataService.Response> {

    private static final Logger log = LoggerFactory.getLogger(JiraDataService.class);
    private final JiraApiProperties jiraApiProperties;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    /*
     * public JiraDataService(JiraApiProperties jiraProps) {
     * this.jiraApiProperties = jiraProps;
     * this.webClient = WebClient.builder()
     * .baseUrl(jiraApiProperties.getApiUrl())
     * .defaultHeaders(headers ->
     * headers.setBasicAuth(jiraApiProperties.getUsername(),
     * jiraApiProperties.getApiToken()))
     * .build();
     * }
     */

    /*
     * public JiraDataService(
     * JiraApiProperties jiraApiProperties,
     * WebClient jiraWebClient) {
     * 
     * this.jiraApiProperties = jiraApiProperties;
     * log.info("Username : {}", jiraApiProperties.getUsername());
     * log.info("Api URL  : {}", jiraApiProperties.getApiUrl());
     * log.info("Token    : {}", jiraApiProperties.getApiToken());
     * this.webClient = jiraWebClient.mutate()
     * .baseUrl(jiraApiProperties.getApiUrl())
     * .defaultHeaders(headers ->
     * headers.setBasicAuth(
     * jiraApiProperties.getUsername(),
     * jiraApiProperties.getApiToken()))
     * .build();
     * }
     */

    public JiraDataService(
            JiraApiProperties jiraApiProperties,
            WebClient jiraWebClient,
            ObjectMapper objectMapper) {

        this.jiraApiProperties = jiraApiProperties;
        this.objectMapper = objectMapper;

        this.webClient = jiraWebClient.mutate()
                .baseUrl(jiraApiProperties.getApiUrl())
                .defaultHeaders(headers -> headers.setBasicAuth(
                        jiraApiProperties.getUsername(),
                        jiraApiProperties.getApiToken()))
                .build();
    }

    @Override
    public Response apply(Request request) {
        try {
            log.info("Fetching details for story: {}", request.storyKey());

            String issueEndpoint = "/rest/api/2/issue/" + request.storyKey();

            String jsonResponse = webClient.get()
                    .uri(issueEndpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Log the full JSON response
            log.info("Full Jira Issue JSON Response: {}", jsonResponse);

            // Parse the JSON response to extract the acceptance criteria
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String description = extractAcceptanceCriteria(rootNode);

            // Create and return Response record
            return new Response(request.storyKey(), description);

        } catch (WebClientResponseException e) {
            log.error("Error response from Jira API: {}", e.getResponseBodyAsString(), e);
            throw new RuntimeException("Failed to fetch Jira issue details for story: " + request.storyKey(), e);
        } catch (Exception e) {
            log.error("Error fetching Jira issue details for story: {}", request.storyKey(), e);
            throw new RuntimeException("Failed to fetch Jira issue details for story: " + request.storyKey(), e);
        }
    }

    private String extractAcceptanceCriteria(JsonNode rootNode) {

        StringBuilder sb = new StringBuilder();

        try {

            JsonNode description = rootNode.path("fields")
                    .path("description");

            extractText(description, sb);

        } catch (Exception e) {

            log.error(
                    "Error extracting acceptance criteria",
                    e);
        }

        return sb.toString().trim();
    }

    private void extractText(
            JsonNode node,
            StringBuilder sb) {

        if (node == null || node.isMissingNode()) {
            return;
        }

        if (node.has("text")) {

            sb.append(
                    node.get("text").asText())
                    .append(" ");
        }

        if (node.isArray()) {

            for (JsonNode child : node) {

                extractText(child, sb);
            }

        } else {

            node.fields().forEachRemaining(
                    entry -> extractText(
                            entry.getValue(),
                            sb));
        }
    }

    public record Request(String storyKey) {
    }

    public record Response(String storyKey, String acceptanceCriteria) {
    }
}
