package com.genai.ollamarestapi.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.genai.ollamarestapi.model.adf.AdfDocument;
import com.genai.ollamarestapi.model.adf.AdfParagraph;
import com.genai.ollamarestapi.model.adf.AdfText;
import com.genai.ollamarestapi.model.jira.JiraIssueRequest;

@Component
public class JiraPayloadBuilder {

    public JiraIssueRequest build(
            String projectKey,
            String summary,
            String description,
            String issueTypeId) {

        AdfText text = new AdfText(description);

        AdfParagraph paragraph = new AdfParagraph();
        paragraph.setContent(List.of(text));

        AdfDocument doc = new AdfDocument();
        doc.setContent(List.of(paragraph));

        JiraIssueRequest.Fields fields =
                new JiraIssueRequest.Fields();

        fields.setProject(
                new JiraIssueRequest.Project(projectKey));

        fields.setSummary(summary);

        fields.setDescription(doc);

        fields.setIssuetype(
                new JiraIssueRequest.IssueType(issueTypeId));

        JiraIssueRequest request =
                new JiraIssueRequest();

        request.setFields(fields);

        return request;
    }
}