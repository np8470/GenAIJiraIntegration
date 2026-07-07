package com.genai.ollamarestapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsResponse {

    private long totalGenerations;

    private long totalTestCases;

    private long successfulRuns;

    private long failedRuns;

    private double successRate;

    private double averageResponseTime;

    private long uploadedToJira;

    private String mostUsedAiModel;

}