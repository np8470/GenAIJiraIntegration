package com.genai.ollamarestapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.genai.ollamarestapi.model.ai.TestCase;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraService {

  private final WebClient jiraWebClient;

  @Value("${jira.apiUrl}")
  private String jiraUrl;

  public String getBrowseUrl(String issueKey) {
    return jiraUrl + "/browse/" + issueKey;
  }

  public String createTestCase(String projectKey, TestCase tc) {
    String jiraDescription = buildJiraDescription(tc);
    String body = """
        {
          "fields": {
            "project": {
              "key": "%s"
            },
            "summary": "%s",
            "issuetype": {
              "id": "10041"
            },
            "description": {
              "type": "doc",
              "version": 1,
              "content": [
                {
                  "type": "paragraph",
                  "content": [
                    {
                      "type": "text",
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
          }
        }
        """.formatted(
        projectKey,
        escapeJson(tc.getTitle()),
        escapeJson(jiraDescription));

    String response = jiraWebClient.post()
        .uri("/rest/api/3/issue")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(String.class)
        .block();
    log.info("Jira Payload: {}", body);
    return extractKey(response);
  }

  private String escapeJson(String value) {

    if (value == null) {
      return "";
    }

    return value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "");
  }

  public void linkIssue(String storyKey, String testCaseKey) {

    String body = """
        {
          "type": { "name": "Relates" },
          "inwardIssue": { "key": "%s" },
          "outwardIssue": { "key": "%s" }
        }
        """.formatted(storyKey, testCaseKey);

    jiraWebClient.post()
        .uri("/rest/api/3/issueLink")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(Void.class)
        .block();

    log.info("Linked Jira issue {} -> {}", storyKey, testCaseKey);
  }

  private String extractKey(String response) {
    try {
      return new JSONObject(response).getString("key");
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse Jira response", e);
    }
  }

  String buildJiraDescription(TestCase tc) {

    StringBuilder sb = new StringBuilder();

    sb.append("Description\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getDescription()));

    sb.append("\n\nPriority\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getPriority()));

    sb.append("\n\nType\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getType()));

    sb.append("\n\nPrecondition\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getPrecondition()));

    sb.append("\n\nSteps\n");
    sb.append("-------------------------\n");

    if (tc.getSteps() != null) {

      int i = 1;

      for (String step : tc.getSteps()) {

        sb.append(i++)
            .append(". ")
            .append(step)
            .append("\n");
      }
    }

    sb.append("\nExpected Result\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getExpectedResult()));

    return sb.toString();
  }

  private String nullToEmpty(String value) {
    return value == null ? "" : value;
  }

}