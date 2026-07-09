package com.genai.ollamarestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.genai.ollamarestapi.model.ai.TestCase;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequest {

    private String storyKey;

    private List<TestCase> testCases;

}