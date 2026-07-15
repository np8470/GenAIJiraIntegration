package com.genai.ollamarestapi.export.common;

import com.genai.ollamarestapi.model.ai.TestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ReportContextBuilder {

    private ReportContextBuilder() {
    }

    public static ReportContext build(

            String storyKey,

            List<TestCase> testCases) {

        int high = 0;
        int medium = 0;
        int low = 0;

        int ui = 0;
        int api = 0;
        int selenium = 0;
        int other = 0;

        for (TestCase tc : testCases) {

            //----------------------------------
            // Priority
            //----------------------------------

            if (tc.getPriority() != null) {

                switch (tc.getPriority().toUpperCase()) {

                    case "HIGH":
                        high++;
                        break;

                    case "MEDIUM":
                        medium++;
                        break;

                    case "LOW":
                        low++;
                        break;

                }

            }

            //----------------------------------
            // Type
            //----------------------------------

            if (tc.getType() != null) {

                switch (tc.getType().toUpperCase()) {

                    case "UI":
                        ui++;
                        break;

                    case "API":
                        api++;
                        break;

                    case "SELENIUM":
                        selenium++;
                        break;

                    default:
                        other++;

                }

            }

        }

        ReportStatistics statistics =
                ReportStatistics.builder()

                        .total(testCases.size())

                        .high(high)

                        .medium(medium)

                        .low(low)

                        .ui(ui)

                        .api(api)

                        .selenium(selenium)

                        .other(other)

                        .build();

        ReportMetadata metadata =
                ReportMetadata.builder()

                        .storyKey(storyKey)

                        .generatedBy(
                                BrandingConfig.GENERATED_BY)

                        .generatedDate(

                                LocalDateTime.now()

                                        .format(

                                                DateTimeFormatter.ofPattern(

                                                        "dd-MMM-yyyy HH:mm:ss"

                                                )

                                        )

                        )

                        .version(
                                BrandingConfig.VERSION)

                        .website(
                                BrandingConfig.WEBSITE)

                        .reportTitle(
                                BrandingConfig.REPORT_TITLE)

                        .jiraUrl(

                                BrandingConfig.JIRA_BASE_URL

                                        + storyKey

                        )

                        .build();

        return ReportContext.builder()

                .metadata(metadata)

                .statistics(statistics)

                .testCases(testCases)

                .build();

    }

}