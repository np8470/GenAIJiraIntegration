package com.genai.ollamarestapi.ai;

import java.util.List;
import java.util.Map;

public final class JsonSchemaFactory {

    private JsonSchemaFactory() {
    }

    public static Map<String, Object> testCaseSchema() {

        return Map.of(

                "type", "array",

                "items", Map.of(

                        "type", "object",

                        "additionalProperties", false,

                        "properties", Map.of(

                                "id",
                                Map.of("type", "string"),

                                "title",
                                Map.of("type", "string"),

                                "description",
                                Map.of("type", "string"),

                                "priority",
                                Map.of("type", "string"),

                                "type",
                                Map.of("type", "string"),

                                "precondition",
                                Map.of("type", "string"),

                                "steps",
                                Map.of(
                                        "type", "array",
                                        "items",
                                        Map.of("type", "string")),

                                "expectedResult",
                                Map.of("type", "string")),

                        "required", List.of(
                                "id",
                                "title",
                                "description",
                                "priority",
                                "type",
                                "precondition",
                                "steps",
                                "expectedResult")));
    }
}