package com.genai.ollamarestapi.model.jira;

import com.genai.ollamarestapi.model.adf.AdfDocument;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JiraIssueRequest {

    private Fields fields;

    @Data
    public static class Fields {

        private Project project;

        private String summary;

        private AdfDocument description;

        private IssueType issuetype;
    }

    @Data
    @AllArgsConstructor
    public static class Project {
        private String key;
    }

    @Data
    @AllArgsConstructor
    public static class IssueType {
        private String id;
    }
}
