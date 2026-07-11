package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.model.ai.TestCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TestCaseNormalizerService {

    /**
     * Normalize newly generated test cases.
     */
    public List<TestCase> normalizeGenerated(List<TestCase> testCases) {

        testCases.forEach(this::normalizeGenerated);

        return testCases;
    }

    /**
     * Normalize one generated test case.
     */
    public TestCase normalizeGenerated(TestCase tc) {

        if (tc.getClientId() == null || tc.getClientId().isBlank()) {

            tc.setClientId(UUID.randomUUID().toString());

        }

        if (tc.getTitle() == null || tc.getTitle().isBlank()) {

            if (tc.getSteps() != null && !tc.getSteps().isEmpty()) {

                String title = tc.getSteps().get(0);

                if (title.length() > 70) {

                    title = title.substring(0, 70);

                }

                tc.setTitle(title);

            }
            else {

                tc.setTitle("Generated Test Case");

            }

        }

        if (tc.getDescription() == null || tc.getDescription().isBlank()) {

            tc.setDescription(tc.getTitle());

        }

        if (tc.getPriority() == null || tc.getPriority().isBlank()) {

            tc.setPriority("Medium");

        }

        if (tc.getType() == null || tc.getType().isBlank()) {

            tc.setType("Functional");

        }

        return tc;

    }

    /**
     * Preserve identity during regeneration.
     */
    public TestCase normalizeRegenerated(
            TestCase original,
            TestCase regenerated) {

        regenerated.setClientId(original.getClientId());

        regenerated.setId(original.getId());

        normalizeGenerated(regenerated);

        return regenerated;

    }

}