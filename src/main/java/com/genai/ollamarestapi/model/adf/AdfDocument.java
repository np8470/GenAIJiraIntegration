package com.genai.ollamarestapi.model.adf;

import lombok.Data;

import java.util.List;

@Data
public class AdfDocument {

    private String type = "doc";

    private int version = 1;

    private List<AdfParagraph> content;
}
