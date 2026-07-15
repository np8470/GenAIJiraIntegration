package com.genai.ollamarestapi.export.common;

import com.genai.ollamarestapi.model.ai.TestCase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportContext {

    private ReportMetadata metadata;

    private ReportStatistics statistics;

    private List<TestCase> testCases;

}