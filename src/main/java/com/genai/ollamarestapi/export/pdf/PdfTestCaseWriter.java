package com.genai.ollamarestapi.export.pdf;

import com.genai.ollamarestapi.model.ai.TestCase;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.util.List;

public final class PdfTestCaseWriter {

    private PdfTestCaseWriter() {
    }

    public static void write(
            Document document,
            List<TestCase> testCases)
            throws Exception {

        int index = 1;

        for (TestCase tc : testCases) {

            PdfPTable table = new PdfPTable(2);

            table.setWidthPercentage(100);

            table.setSpacingBefore(12);

            table.setSpacingAfter(12);

            table.setWidths(new float[]{25,75});

            //-----------------------------
            // Card Title
            //-----------------------------

            PdfPCell title =
                    new PdfPCell(

                            new Phrase(
                                    "TEST CASE " + index++,
                                    PdfStyles.SECTION));

            title.setColspan(2);

            title.setBackgroundColor(
                    new Color(13,110,253));

            title.setBorderColor(Color.GRAY);

            title.setPadding(8);

            table.addCell(title);

            //-----------------------------
            // ID
            //-----------------------------

            addLabel(table,"ID");

            addValue(table,
                    safe(tc.getId()),
                    PdfStyles.NORMAL);

            //-----------------------------
            // Title
            //-----------------------------

            addLabel(table,"Title");

            addValue(table,
                    safe(tc.getTitle()),
                    PdfStyles.BOLD);

            //-----------------------------
            // Priority
            //-----------------------------

            addLabel(table,"Priority");

            addPriorityCell(
                    table,
                    tc.getPriority());

            //-----------------------------
            // Type
            //-----------------------------

            addLabel(table,"Type");

            addValue(table,
                    safe(tc.getType()),
                    PdfStyles.NORMAL);

            //-----------------------------
            // Description
            //-----------------------------

            addLabel(table,"Description");

            addValue(table,
                    safe(tc.getDescription()),
                    PdfStyles.NORMAL);

            //-----------------------------
            // Precondition
            //-----------------------------

            addLabel(table,"Precondition");

            addValue(table,
                    safe(tc.getPrecondition()),
                    PdfStyles.NORMAL);

            //-----------------------------
            // Steps
            //-----------------------------

            addLabel(table,"Steps");

            addValue(
                    table,
                    buildSteps(tc),
                    PdfStyles.NORMAL);

            //-----------------------------
            // Expected Result
            //-----------------------------

            addLabel(table,"Expected Result");

            addValue(
                    table,
                    safe(tc.getExpectedResult()),
                    PdfStyles.NORMAL);

            document.add(table);

            document.add(
                    new Paragraph(" "));
        }

    }

    //--------------------------------------------------------

    private static void addLabel(
            PdfPTable table,
            String label) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                label,
                                PdfStyles.BOLD));

        cell.setBackgroundColor(
                new Color(240,240,240));

        cell.setPadding(6);

        table.addCell(cell);

    }

    //--------------------------------------------------------

    private static void addValue(
            PdfPTable table,
            String value,
            Font font) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                value,
                                font));

        cell.setPadding(6);

        table.addCell(cell);

    }

    //--------------------------------------------------------

    private static void addPriorityCell(
            PdfPTable table,
            String priority) {

        Font font;

        Color background;

        if(priority==null){

            font=PdfStyles.NORMAL;

            background=Color.WHITE;

        }

        else if(priority.equalsIgnoreCase("HIGH")){

            font=PdfStyles.HIGH;

            background=new Color(255,230,230);

        }

        else if(priority.equalsIgnoreCase("MEDIUM")){

            font=PdfStyles.MEDIUM;

            background=new Color(255,245,220);

        }

        else{

            font=PdfStyles.LOW;

            background=new Color(230,255,230);

        }

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                safe(priority),
                                font));

        cell.setBackgroundColor(background);

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER);

        cell.setPadding(6);

        table.addCell(cell);

    }

    //--------------------------------------------------------

    private static String buildSteps(
            TestCase tc){

        if(tc.getSteps()==null ||
                tc.getSteps().isEmpty()){

            return "";

        }

        StringBuilder sb =
                new StringBuilder();

        int i=1;

        for(String step : tc.getSteps()){

            sb.append(i++)
                    .append(". ")
                    .append(step)
                    .append("\n");

        }

        return sb.toString();

    }

    //--------------------------------------------------------

    private static String safe(
            String value){

        return value==null ? "" : value;

    }

}