package com.genai.ollamarestapi.export;

import com.genai.ollamarestapi.model.ai.TestCase;
import org.apache.poi.xwpf.usermodel.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class WordSummaryBuilder {

    private WordSummaryBuilder() {
    }

    public static void create(

            XWPFDocument document,

            String storyKey,

            List<TestCase> testCases) {

        // ----------------------------------------
        // Calculate Statistics
        // ----------------------------------------

        int high = 0;
        int medium = 0;
        int low = 0;

        int ui = 0;
        int api = 0;
        int selenium = 0;
        int other = 0;

        for (TestCase tc : testCases) {

            // ------------------------------------
            // Priority
            // ------------------------------------

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

            // ------------------------------------
            // Type
            // ------------------------------------

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

        // ----------------------------------------
        // Page Break
        // ----------------------------------------

        XWPFParagraph page = document.createParagraph();

        page.setPageBreak(true);

        // ----------------------------------------
        // Title
        // ----------------------------------------

        XWPFParagraph heading = document.createParagraph();

        heading.setAlignment(
                ParagraphAlignment.CENTER);

        XWPFRun title = heading.createRun();

        WordStyles.title(title);

        title.setText("REPORT SUMMARY");

        document.createParagraph();

        // ----------------------------------------
        // Summary Table
        // ----------------------------------------

        XWPFTable table = document.createTable(11, 2);

        WordTableFormatter.format(table);

        table.setWidth("100%");

        /* addRow(table,0,"Story Key",storyKey); */

        // ---------------------------------------
        // Story Key
        // ---------------------------------------

        addRow(
                table,
                0,
                "Story Key",
                storyKey);

        // ---------------------------------------
        // Jira URL
        // ---------------------------------------

        addRow(
                table,
                1,
                "Jira URL",
                WordConstants.JIRA_BROWSE_URL + storyKey);

        addRow(table, 2, "Generated Test Cases",
                String.valueOf(testCases.size()));

        addRow(table, 3, "High Priority",
                String.valueOf(high));

        addRow(table, 4, "Medium Priority",
                String.valueOf(medium));

        addRow(table, 5, "Low Priority",
                String.valueOf(low));

        addRow(table, 6, "UI Test Cases",
                String.valueOf(ui));

        addRow(table, 7, "API Test Cases",
                String.valueOf(api));

        addRow(table, 8, "Selenium Test Cases",
                String.valueOf(selenium));

        addRow(table, 9, "Other Test Cases",
                String.valueOf(other));

        addRow(

                table,

                10,

                "Generated",

                LocalDateTime.now()

                        .format(

                                DateTimeFormatter.ofPattern(

                                        "dd-MMM-yyyy HH:mm:ss"

                                )

                        )

        );

        // ----------------------------------------
        // Report Information
        // ----------------------------------------

        document.createParagraph();

        XWPFParagraph info = document.createParagraph();

        XWPFRun run = info.createRun();

        WordStyles.heading(run);

        run.setText("Report Information");

        XWPFParagraph paragraph = document.createParagraph();

        XWPFRun body = paragraph.createRun();

        WordStyles.normal(body);

        body.setText(

                "Generated By : TestPilot AI Enterprise\n\n"

                        + "Version : Enterprise Edition v1.0\n\n"

                        + "Execution Status : READY FOR EXECUTION\n\n"

                        + "The generated report contains AI-created "

                        + "functional test cases suitable for manual "

                        + "execution, automation implementation, "

                        + "Jira upload, Selenium development and "

                        + "API validation."

        );

        // ----------------------------------------
        // Footer Message
        // ----------------------------------------

        document.createParagraph();

        XWPFParagraph footer = document.createParagraph();

        footer.setAlignment(
                ParagraphAlignment.CENTER);

        XWPFRun footerRun = footer.createRun();

        footerRun.setItalic(true);

        footerRun.setFontSize(10);

        footerRun.setColor("808080");

        footerRun.setText(

                "Generated automatically by TestPilot AI Enterprise"

        );

    }

    // ----------------------------------------------------------

    private static void addRow(

            XWPFTable table,

            int row,

            String label,

            String value) {

        XWPFTableRow tableRow = table.getRow(row);

        // ---------------- Label ----------------

        XWPFRun left = tableRow

                .getCell(0)

                .getParagraphs()

                .get(0)

                .createRun();

        WordStyles.heading(left);

        left.setText(label);

        // ---------------- Value ----------------

        XWPFRun right = tableRow

                .getCell(1)

                .getParagraphs()

                .get(0)

                .createRun();

        WordStyles.normal(right);

        right.setText(value);

    }

}