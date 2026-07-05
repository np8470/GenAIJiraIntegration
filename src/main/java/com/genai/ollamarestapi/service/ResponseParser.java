package com.genai.ollamarestapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseParser {

    private final ObjectMapper objectMapper;

    /**
     * Parse Ollama response into TestCase list.
     */
    @Audit(action = AuditAction.PARSE_OLLAMA_RES_TO_TESTCASE ,message = "Parse Ollama response  into Test Cases list")
    public List<TestCase> parse(String response) {

        try {

            if (response == null || response.isBlank()) {
                throw new RuntimeException("AI response is empty.");
            }

            log.info("Raw Response:\n{}", response);

            String json = extractJson(response);

            JsonNode root = objectMapper.readTree(json);

            List<TestCase> testCases = new ArrayList<>();

            if (root.isArray()) {

                for (JsonNode node : root) {
                    testCases.add(parseTestCase(node));
                }

            } else if (root.isObject()) {

                testCases.add(parseTestCase(root));

            } else {

                throw new RuntimeException("Unsupported JSON returned by AI.");
            }

            log.info("Parsed {} test case(s).", testCases.size());

            return testCases;

        } catch (Exception ex) {

            log.error("Unable to parse AI response.", ex);

            throw new RuntimeException("Unable to parse AI response.", ex);
        }
    }

    /**
     * Parse one JSON object into TestCase.
     */
    @Audit(action = AuditAction.PARSE_JSON_OBJ_TO_TESTCASE, message = "Parse one JSON object {0} into TestCase." )
    private TestCase parseTestCase(JsonNode node) {

        TestCase tc = new TestCase();

        tc.setId(getText(node, "id"));
        tc.setTitle(getText(node, "title"));
        tc.setDescription(getText(node, "description"));
        tc.setPriority(getText(node, "priority"));
        tc.setType(getText(node, "type"));
        tc.setPrecondition(getText(node, "precondition"));

        //-----------------------------
        // Steps
        //-----------------------------
        List<String> steps = new ArrayList<>();

        JsonNode stepNode = node.get("steps");

        if (stepNode != null) {

            if (stepNode.isArray()) {

                for (JsonNode s : stepNode) {
                    steps.add(s.asText());
                }

            } else {

                steps.add(stepNode.asText());
            }
        }

        tc.setSteps(steps);

        //-----------------------------
        // Expected Result
        //-----------------------------
        JsonNode expectedNode = node.get("expectedResult");

        if (expectedNode != null) {

            if (expectedNode.isArray()) {

                StringBuilder sb = new StringBuilder();

                Iterator<JsonNode> iterator = expectedNode.iterator();

                while (iterator.hasNext()) {

                    sb.append(iterator.next().asText());

                    if (iterator.hasNext()) {
                        sb.append("\n");
                    }
                }

                tc.setExpectedResult(sb.toString());

            } else {

                tc.setExpectedResult(expectedNode.asText());
            }
        }

        return tc;
    }

    /**
     * Reads a String field safely.
     */
    private String getText(JsonNode node, String field) {

        JsonNode value = node.get(field);

        if (value == null || value.isNull()) {
            return "";
        }

        return value.asText();
    }

    /**
     * Removes markdown and extracts JSON.
     */
    private String extractJson(String response) {

        response = response.trim();

        response = response.replace("```json", "");
        response = response.replace("```", "");
        response = response.trim();

        int objectStart = response.indexOf('{');
        int arrayStart = response.indexOf('[');

        if (arrayStart != -1 &&
                (objectStart == -1 || arrayStart < objectStart)) {

            int arrayEnd = response.lastIndexOf(']');

            if (arrayEnd == -1) {
                throw new RuntimeException("Invalid JSON Array.");
            }

            return response.substring(arrayStart, arrayEnd + 1);
        }

        if (objectStart != -1) {

            int objectEnd = response.lastIndexOf('}');

            if (objectEnd == -1) {
                throw new RuntimeException("Invalid JSON Object.");
            }

            return response.substring(objectStart, objectEnd + 1);
        }

        throw new RuntimeException("No JSON found in AI response.");
    }
}