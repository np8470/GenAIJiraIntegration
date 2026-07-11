package com.genai.ollamarestapi.model;

import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO.
 * Returned after AI regeneration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexedTestCaseResult {

    /**
     * Original index in generatedTestCases[].
     */
    private Integer index;

    /**
     * Regenerated test case.
     */
    private TestCase testCase;

    /**
     * Whether regeneration succeeded.
     */
    private boolean success;

    /**
     * Error message if regeneration failed.
     */
    private String message;

    /**
     * Time spent regenerating this test case.
     */
    private long durationMillis;

    private String clientId;

}