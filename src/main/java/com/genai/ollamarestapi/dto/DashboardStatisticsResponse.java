package com.genai.ollamarestapi.dto;

import java.util.List;

import com.genai.ollamarestapi.audit.AuditLog;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsResponse {

    //----------------------------
    // Overview
    //----------------------------

    private long totalGenerations;

    private long successfulRuns;

    private long failedRuns;

    private double successRate;

    //----------------------------
    // AI
    //----------------------------

    private long totalTestCases;

    private double averageResponseTime;

    private String mostUsedAiModel;

    //----------------------------
    // Upload
    //----------------------------

    private long uploadedToJira;

    private long pendingUpload;

    //----------------------------
    // Type
    //----------------------------

    private long uiReports;

    private long apiReports;

    private long seleniumReports;

    //----------------------------
    // Priority
    //----------------------------

    private long highPriority;

    private long mediumPriority;

    private long lowPriority;

    private List<AuditLog> recentLogs;

}