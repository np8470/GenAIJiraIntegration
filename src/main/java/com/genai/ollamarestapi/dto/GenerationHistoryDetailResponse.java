package com.genai.ollamarestapi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationHistoryDetailResponse {

    private Long id;

    private String storyKey;

    private String acceptanceCriteria;

    private String generationType;

    private String aiModel;

    private Integer testCaseCount;

    private Long responseTimeMs;

    private String status;

    private LocalDateTime createdAt;

    private List<GenerationTestCaseResponse> testCases;
}