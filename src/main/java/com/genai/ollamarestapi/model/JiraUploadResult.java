package com.genai.ollamarestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JiraUploadResult {

    private String issueKey;

    private String issueUrl;

    //private boolean success;

    //private String errorMessage;

}