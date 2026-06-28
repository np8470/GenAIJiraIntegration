package com.genai.ollamarestapi.model;

import java.util.List;

import com.genai.ollamarestapi.model.ai.TestCase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateResponse {

    private String storyKey;
    private String generationType;
    private String generatedContent;
    private List<TestCase> testCases;

}