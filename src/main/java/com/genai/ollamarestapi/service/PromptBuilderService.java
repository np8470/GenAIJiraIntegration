package com.genai.ollamarestapi.service;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.model.GenerationType;

@Service
public class PromptBuilderService {

    public String buildPrompt(
            String story,
            GenerationType type) {

        return switch (type) {

            case TEST_CASES ->
                manualPrompt(story);

            case API_TEST_CASES ->
                apiPrompt(story);

            case SELENIUM_SCRIPT ->
                automationPrompt(story);

            default ->
                manualPrompt(story);
        };
    }

    private String manualPrompt(String story) {

        return """
                Generate Manual QA Test Cases.

                IMPORTANT

                Return ONLY JSON array.
                Example:
                                                                    [
                                                                      {
                                                                        "id":"TC_001",
                                                                        "title":"Verify Login",
                                                                        "description":"Verify login functionality",
                                                                        "priority":"High",
                                                                        "type":"Functional",
                                                                        "precondition":"User exists",
                                                                        "steps":[
                                                                          "Open login page",
                                                                          "Enter username",
                                                                          "Enter password",
                                                                          "Click login"
                                                                        ],
                                                                        "expectedResult":"User logged in successfully"
                                                                      }
                                                                    ]

                Each JSON object MUST contain:

                id
                title
                description
                priority
                type
                precondition
                steps
                expectedResult

                User Story:
                %s
                """.formatted(story);

    }

    private String apiPrompt(String story) {

        return """
                                Generate REST API Test Cases.

                                Cover

                                Positive

                                Negative

                                Authentication

                                Authorization

                                Headers

                                Status Codes

                                Boundary Values

                                Validation

                                Error Handling

                                Return ONLY JSON array.
                                IMPORTANT RULES:
                IMPORTANT

                Your response MUST satisfy ALL rules.

                1 Return ONLY one JSON array.

                2 Do NOT write explanations.

                3 Do NOT use markdown.

                4 Do NOT use ```json

                5 Do NOT number test cases.

                6 Do NOT say "Here are..."

                7 Response MUST begin with [

                8 Response MUST end with ]

                If you violate these rules the response is invalid.



                                                Example:

                                                [
                                                 {
                                                   "id":"API_TC_001",
                                                   "title":"Create User Success",
                                                   "description":"Verify create user endpoint",
                                                   "priority":"High",
                                                   "type":"API",
                                                   "precondition":"API available",
                                                   "steps":[
                                                      "Send POST /users",
                                                      "Pass valid payload"
                                                   ],
                                                   "expectedResult":"201 Created"
                                                 }
                                                ]
                                Each object contains

                                id
                                title
                                description
                                priority
                                type
                                precondition
                                steps
                                expectedResult

                                Story

                                %s
                                """.formatted(story);

    }

    private String automationPrompt(String story) {

        return """
                Generate Selenium Automation Test Cases.

                Return ONLY JSON.

                Each object contains

                id
                title
                description
                priority
                type
                precondition
                steps
                expectedResult


                Story

                %s
                """.formatted(story);

    }

}