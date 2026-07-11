package com.genai.ollamarestapi.model;

import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Test Case together with its original
 * index in generatedTestCases[].
 *
 * This allows the frontend to replace the correct card
 * after AI regeneration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexedTestCaseRequest {

    /**
     * Original index in generatedTestCases[]
     */
    private Integer index;

    /**
     * Test case to regenerate/upload/etc.
     */
    private TestCase testCase;

    /*
     * Permanent UI identifier.
     */
    private String clientId;

}