package com.genai.ollamarestapi.export.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportStatistics {

    private int total;

    private int high;

    private int medium;

    private int low;

    private int ui;

    private int api;

    private int selenium;

    private int other;

}