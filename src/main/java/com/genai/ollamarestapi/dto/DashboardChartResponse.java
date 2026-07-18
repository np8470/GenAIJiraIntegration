package com.genai.ollamarestapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardChartResponse {

    //----------------
    // Priority
    //----------------

    private long highPriority;

    private long mediumPriority;

    private long lowPriority;

    //----------------
    // Type
    //----------------

    private long ui;

    private long api;

    private long selenium;

}
