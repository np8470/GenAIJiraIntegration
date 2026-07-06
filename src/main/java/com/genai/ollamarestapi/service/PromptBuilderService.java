package com.genai.ollamarestapi.service;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.model.GenerationType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PromptBuilderService {
  
  @Audit(action = AuditAction.BUILD_PROMPT, message = "Build prompt for Work Item id {0} and Generation Type {1}")
  public String buildPrompt(
      String story,
      GenerationType type) {
    log.info("Build Prompt for Work Item id and Generation Type {} -> {}",story, type);
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

  @Audit(action = AuditAction.MANUAL_PROMPT, message = "UI test cases prompt for Work Item id {0}")
  private String manualPrompt(String story) {

    return """

        Generate comprehensive Manual test cases.

        IMPORTANT RULES

        1. Return ONLY valid JSON.
        2. Do NOT use markdown.
        3. Do NOT use explanations.
        4. Do NOT number test cases.
        5. Response MUST start with [
        6. Response MUST end with ]

        Schema

        [
          {
            "id":"TC_001",
            "title":"",
            "description":"",
            "priority":"High",
            "type":"Functional",
            "precondition":"",
            "steps":[
              ""
            ],
            "expectedResult":""
          }
        ]

        expectedResult MUST be a string.

        DO NOT return an array.

        Correct:

        "expectedResult":"Login successful"

        Wrong:

        "expectedResult":[
          "Login successful"
        ]

                                                        User Story:
                                                        %s
                                                        """.formatted(story);

  }

  @Audit(action = AuditAction.API_PROMPT, message = "API test cases prompt for Work Item id {0}")
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

  @Audit(action = AuditAction.SELENIUM_SCRIPT_PROMPT, message = "Selenium script for Work Item id {0}")
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