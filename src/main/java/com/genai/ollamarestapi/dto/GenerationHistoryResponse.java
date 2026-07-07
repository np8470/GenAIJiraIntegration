package com.genai.ollamarestapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationHistoryResponse {

    private Long id;

    private String storyKey;

    private String storySummary;

    private Integer testCaseCount;

    private String aiModel;

    private Long responseTimeMs;

    private String status;

    private LocalDateTime createdAt;
}