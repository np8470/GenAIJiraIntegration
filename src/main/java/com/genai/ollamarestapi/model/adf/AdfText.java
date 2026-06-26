package com.genai.ollamarestapi.model.adf;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdfText {

    private String type = "text";

    private String text;

    public AdfText(String text) {
        this.text = text;
    }
}