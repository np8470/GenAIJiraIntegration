package com.genai.ollamarestapi.model;

import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.Data;

import java.util.List;

@Data
public class ExportRequest {

    private String storyKey;

    private List<TestCase> testCases;

}