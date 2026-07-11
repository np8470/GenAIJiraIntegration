package com.genai.ollamarestapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkRegenerateResponse {

    private boolean success;

    private String message;

    /**
     * Results returned to UI.
     */
    private List<IndexedTestCaseResult> items;

}