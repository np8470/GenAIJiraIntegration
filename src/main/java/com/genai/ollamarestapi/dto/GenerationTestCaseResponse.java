package com.genai.ollamarestapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationTestCaseResponse {

    private Long id;

    private String title;

    private String description;

    private String priority;

    private String type;

    private String precondition;

    private String steps;

    private String expectedResult;

    private Boolean uploadedToJira;

    private String jiraTestcaseKey;
}