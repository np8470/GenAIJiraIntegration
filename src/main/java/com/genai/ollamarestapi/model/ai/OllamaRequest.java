package com.genai.ollamarestapi.model.ai;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OllamaRequest {

    private String model;

    private String prompt;

    private boolean stream;

    /**
     * "json" OR JSON Schema
     */
    private Object format;

    /**
     * Optional temperature
     */
    private Double temperature;

    /**
     * Optional seed
     */
    private Integer seed;

    /**
     * Optional keep alive
     */
    private String keep_alive;
}