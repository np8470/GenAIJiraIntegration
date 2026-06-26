package com.genai.ollamarestapi.util;

import java.util.ArrayList;
import java.util.List;

public class TestCaseParser {

    public static List<String> splitTestCases(String content) {

        List<String> testCases = new ArrayList<>();

        String[] split =
                content.split("---");

        for(String tc : split) {

            if(!tc.trim().isEmpty()) {
                testCases.add(tc.trim());
            }
        }

        return testCases;
    }

    public static String extractSummary(String testCase) {

        String[] lines = testCase.split("\n");

        for(String line : lines) {

            if(line.contains("Test Case ID")) {

                return line
                        .replace("**Test Case ID:**","")
                        .trim();
            }
        }

        return "Generated Test Case";
    }
}