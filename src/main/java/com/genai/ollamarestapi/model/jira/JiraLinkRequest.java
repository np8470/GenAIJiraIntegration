package com.genai.ollamarestapi.model.jira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JiraLinkRequest {

    private LinkType type;

    private Issue inwardIssue;

    private Issue outwardIssue;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkType {

        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Issue {

        private String key;
    }
}