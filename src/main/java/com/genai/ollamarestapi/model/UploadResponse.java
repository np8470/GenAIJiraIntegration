package com.genai.ollamarestapi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponse {

    private boolean success;

    private String message;

    private int uploadedCount;

    private int failedCount;

    private List<String> jiraLinks;

}