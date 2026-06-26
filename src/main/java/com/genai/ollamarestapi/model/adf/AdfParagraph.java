package com.genai.ollamarestapi.model.adf;

import lombok.Data;

import java.util.List;

@Data
public class AdfParagraph {

    private String type = "paragraph";

    private List<AdfText> content;
}
