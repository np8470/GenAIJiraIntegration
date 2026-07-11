package com.genai.ollamarestapi.model.ai;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {

    private String id;

    private String title;

    private String description;

    private String priority;

    private String type;

    private String precondition;

    private List<String> steps;

    private String expectedResult;

    /**
     * Stable identifier used by UI.
     * Never changes during the lifetime of the generated test case.
     */
    private String clientId = UUID.randomUUID().toString();
}
