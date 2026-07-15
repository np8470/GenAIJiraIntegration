package com.genai.ollamarestapi.export.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportMetadata {

    private String storyKey;

    private String generatedBy;

    private String generatedDate;

    private String version;

    private String website;

    private String reportTitle;

    private String jiraUrl;

}