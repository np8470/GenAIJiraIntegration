package com.genai.ollamarestapi.export;

import com.genai.ollamarestapi.model.ai.TestCase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class ExcelExporter {

    public byte[] export(String storyKey,
            List<TestCase> testCases)
            throws Exception {

        Workbook workbook = new XSSFWorkbook();

        ExcelStyleFactory styles = new ExcelStyleFactory((XSSFWorkbook) workbook);

        Sheet sheet = workbook.createSheet("AI Test Cases");

        addLogo(
                workbook,
                sheet);

        int headerRow = ExcelHeaderBuilder.build(

                sheet,

                styles,

                storyKey,

                testCases.size()

        );

        createHeader(

                workbook,

                sheet,

                styles,

                headerRow

        );

        int rowIndex = headerRow + 1;

        rowIndex = ExcelDataWriter.write(

                sheet,

                styles,

                testCases,

                rowIndex

        );

        ExcelSummaryBuilder.create(

                workbook,

                sheet,

                styles,

                storyKey,

                testCases,

                rowIndex);

        autoSize(sheet);

        sheet.createFreezePane(

                0,

                8

        );

        sheet.setAutoFilter(

                new CellRangeAddress(

                        7,

                        7,

                        0,

                        7

                )

        );
        applyPriorityFormatting(sheet);
        addWatermark(
                workbook,
                sheet);
        // Enterprise print layout
        ExcelPageSetup.configure(
                workbook,
                sheet,
                rowIndex);

        // Header & Footer
        ExcelHeaderFooter.apply(
                sheet,
                storyKey);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook.write(out);

        workbook.close();

        return out.toByteArray();

    }

    private void createHeader(Workbook workbook, Sheet sheet, ExcelStyleFactory styles, int rowIndex) {

        Row row = sheet.createRow(rowIndex);

        for (int i = 0; i < ExcelConstants.HEADERS.length; i++) {

            Cell cell = row.createCell(i);

            cell.setCellValue(

                    ExcelConstants.HEADERS[i]);

            cell.setCellStyle(

                    styles.getHeaderStyle());

        }

    }

    private void autoSize(
            Sheet sheet) {

        for (int i = 0; i < 8; i++) {

            sheet.autoSizeColumn(i);

            int width = sheet.getColumnWidth(i);

            if (width > 18000) {

                sheet.setColumnWidth(i, 18000);

            }

        }

    }

    private void addLogo(
            Workbook workbook,
            Sheet sheet) {

        try {

            InputStream stream = getClass().getResourceAsStream(
                    "/logo.png");

            if (stream == null) {
                return;
            }

            byte[] bytes = stream.readAllBytes();

            int pictureIdx = workbook.addPicture(
                    bytes,
                    Workbook.PICTURE_TYPE_PNG);

            CreationHelper helper = workbook.getCreationHelper();

            Drawing<?> drawing = sheet.createDrawingPatriarch();

            ClientAnchor anchor = helper.createClientAnchor();

            anchor.setCol1(7);

            anchor.setRow1(0);

            Picture picture = drawing.createPicture(
                    anchor,
                    pictureIdx);

            picture.resize(0.6);

        } catch (Exception ignored) {

        }

    }

    private void applyPriorityFormatting(
            Sheet sheet) {

        SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();

        ConditionalFormattingRule high = scf.createConditionalFormattingRule(
                ComparisonOperator.EQUAL,
                "\"High\"");

        PatternFormatting highFill = high.createPatternFormatting();

        highFill.setFillForegroundColor(
                IndexedColors.RED.getIndex());

        highFill.setFillPattern(
                PatternFormatting.SOLID_FOREGROUND);

        ConditionalFormattingRule medium = scf.createConditionalFormattingRule(
                ComparisonOperator.EQUAL,
                "\"Medium\"");

        PatternFormatting mediumFill = medium.createPatternFormatting();

        mediumFill.setFillBackgroundColor(
                IndexedColors.LIGHT_ORANGE.getIndex());

        mediumFill.setFillPattern(
                PatternFormatting.SOLID_FOREGROUND);

        ConditionalFormattingRule low = scf.createConditionalFormattingRule(
                ComparisonOperator.EQUAL,
                "\"Low\"");

        PatternFormatting lowFill = low.createPatternFormatting();

        lowFill.setFillBackgroundColor(
                IndexedColors.LIGHT_GREEN.getIndex());

        lowFill.setFillPattern(
                PatternFormatting.SOLID_FOREGROUND);

        CellRangeAddress[] regions = {

                new CellRangeAddress(
                        3,
                        1000,
                        3,
                        3)

        };

        ConditionalFormattingRule[] rules = {
                high,
                medium,
                low
        };

        scf.addConditionalFormatting(
                regions,
                rules);

    }

    private void addWatermark(
            Workbook workbook,
            Sheet sheet) {

        Row row = sheet.createRow(50);

        Cell cell = row.createCell(2);

        cell.setCellValue(
                "Generated by TestPilot AI");

        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 32);

        font.setItalic(true);

        font.setColor(
                IndexedColors.GREY_40_PERCENT.getIndex());

        CellStyle style = workbook.createCellStyle();

        style.setFont(font);

        cell.setCellStyle(style);

    }
}
