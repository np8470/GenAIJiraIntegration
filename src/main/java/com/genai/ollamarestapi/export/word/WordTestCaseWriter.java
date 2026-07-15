package com.genai.ollamarestapi.export.word;

import com.genai.ollamarestapi.model.ai.TestCase;
import org.apache.poi.xwpf.usermodel.*;

import java.util.List;

public final class WordTestCaseWriter {

    private WordTestCaseWriter() {
    }

    public static void write(

            XWPFDocument document,

            List<TestCase> testCases) {

        int index = 1;

        for (TestCase tc : testCases) {

            //------------------------------------------------
            // Test Case Heading
            //------------------------------------------------

            XWPFParagraph heading =
                    document.createParagraph();

            heading.setSpacingBefore(300);

            heading.setSpacingAfter(150);

            XWPFRun run =
                    heading.createRun();

            WordStyles.heading(run);

            run.setText(
                    "TEST CASE " + index++);

            //------------------------------------------------
            // Table
            //------------------------------------------------

            XWPFTable table =
                    document.createTable(7,2);
            WordTableFormatter.format(table);

            table.setWidth("100%");

            createRow(

                    table,

                    0,

                    "ID",

                    tc.getId()

            );

            createRow(

                    table,

                    1,

                    "Title",

                    tc.getTitle()

            );

            createPriorityRow(

                    table,

                    2,

                    tc.getPriority()

            );

            createRow(

                    table,

                    3,

                    "Type",

                    tc.getType()

            );

            createRow(

                    table,

                    4,

                    "Precondition",

                    tc.getPrecondition()

            );

            createStepsRow(

                    table,

                    5,

                    tc.getSteps()

            );

            createRow(

                    table,

                    6,

                    "Expected Result",

                    tc.getExpectedResult()

            );

            //------------------------------------------------
            // Blank Line
            //------------------------------------------------

            document.createParagraph();

        }

    }

    //---------------------------------------------------------
    // Standard Row
    //---------------------------------------------------------

    private static void createRow(

            XWPFTable table,

            int row,

            String label,

            String value) {

        XWPFTableRow tableRow =
                table.getRow(row);

        //---------------- Label ----------------

        XWPFRun left =
                tableRow
                        .getCell(0)
                        .getParagraphs()
                        .get(0)
                        .createRun();

        WordStyles.heading(left);

        left.setText(label);

        //---------------- Value ----------------

        XWPFRun right =
                tableRow
                        .getCell(1)
                        .getParagraphs()
                        .get(0)
                        .createRun();

        WordStyles.normal(right);

        right.setText(
                value == null ? "" : value);

    }

    //---------------------------------------------------------
    // Priority Row
    //---------------------------------------------------------

    private static void createPriorityRow(

            XWPFTable table,

            int row,

            String priority) {

        XWPFTableRow tableRow =
                table.getRow(row);

        XWPFRun left =
                tableRow
                        .getCell(0)
                        .getParagraphs()
                        .get(0)
                        .createRun();

        WordStyles.heading(left);

        left.setText("Priority");

        XWPFRun right =
                tableRow
                        .getCell(1)
                        .getParagraphs()
                        .get(0)
                        .createRun();

        if(priority==null){

            WordStyles.normal(right);

            right.setText("");

            return;

        }

        switch(priority.toUpperCase()){

            case "HIGH":

                WordStyles.priorityHigh(right);

                break;

            case "MEDIUM":

                WordStyles.priorityMedium(right);

                break;

            case "LOW":

                WordStyles.priorityLow(right);

                break;

            default:

                WordStyles.normal(right);

        }

        right.setText(priority);

    }

    //---------------------------------------------------------
    // Steps Row
    //---------------------------------------------------------

    private static void createStepsRow(

            XWPFTable table,

            int row,

            List<String> steps){

        XWPFTableRow tableRow =
                table.getRow(row);

        XWPFRun left =
                tableRow
                        .getCell(0)
                        .getParagraphs()
                        .get(0)
                        .createRun();

        WordStyles.heading(left);

        left.setText("Steps");

        XWPFTableCell cell =
                tableRow.getCell(1);

        cell.removeParagraph(0);

        if(steps==null || steps.isEmpty()){

            XWPFParagraph paragraph =
                    cell.addParagraph();

            XWPFRun run =
                    paragraph.createRun();

            WordStyles.normal(run);

            run.setText("");

            return;

        }

        int count=1;

        for(String step : steps){

            XWPFParagraph paragraph =
                    cell.addParagraph();

            paragraph.setSpacingAfter(50);

            XWPFRun run =
                    paragraph.createRun();

            WordStyles.normal(run);

            run.setBold(true);

            run.setText(

                    count++ + ". " + step

            );

        }

    }

}