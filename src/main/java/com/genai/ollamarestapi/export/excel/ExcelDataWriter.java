package com.genai.ollamarestapi.export.excel;

import com.genai.ollamarestapi.model.ai.TestCase;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

public class ExcelDataWriter {

    private ExcelDataWriter() {
    }

    public static int write(

            Sheet sheet,

            ExcelStyleFactory styles,

            List<TestCase> testCases,

            int startRow) {

        int rowIndex = startRow;

        for (int i = 0; i < testCases.size(); i++) {

            TestCase tc = testCases.get(i);

            Row row = sheet.createRow(rowIndex++);

            row.setHeightInPoints(60);

            boolean even = i % 2 == 0;

            CellStyle normal =
                    even
                            ? styles.getEvenRowStyle()
                            : styles.getOddRowStyle();

            writeCell(row, 0, tc.getId(), normal);

            writeCell(row, 1, tc.getTitle(), normal);

            writeCell(row, 2, tc.getDescription(), normal);

            writePriority(row, 3, tc.getPriority(), styles);

            writeCell(row, 4, tc.getType(), normal);

            writeCell(row, 5, tc.getPrecondition(), normal);

            writeCell(row, 6, joinSteps(tc.getSteps()), normal);

            writeCell(row, 7, tc.getExpectedResult(), normal);

        }

        return rowIndex;

    }

    private static void writeCell(

            Row row,

            int column,

            String value,

            CellStyle style) {

        Cell cell = row.createCell(column);

        cell.setCellValue(value == null ? "" : value);

        cell.setCellStyle(style);

    }

    private static void writePriority(

            Row row,

            int column,

            String priority,

            ExcelStyleFactory styles) {

        Cell cell = row.createCell(column);

        cell.setCellValue(priority);

        if (priority == null) {

            cell.setCellStyle(
                    styles.getOddRowStyle());

            return;

        }

        switch (priority.toUpperCase()) {

            case "HIGH":

                cell.setCellStyle(
                        styles.getHighPriorityStyle());

                break;

            case "MEDIUM":

                cell.setCellStyle(
                        styles.getMediumPriorityStyle());

                break;

            case "LOW":

                cell.setCellStyle(
                        styles.getLowPriorityStyle());

                break;

            default:

                cell.setCellStyle(
                        styles.getOddRowStyle());

        }

    }

    private static String joinSteps(List<String> steps) {

        if (steps == null || steps.isEmpty()) {

            return "";

        }

        StringBuilder sb = new StringBuilder();

        int i = 1;

        for (String step : steps) {

            sb.append(i++)
                    .append(". ")
                    .append(step)
                    .append("\n");

        }

        return sb.toString();

    }

}