package com.genai.ollamarestapi.export.excel;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Getter
public class ExcelStyleFactory {

    private final Workbook workbook;

    private final CellStyle titleStyle;
    private final CellStyle headerStyle;
    private final CellStyle normalStyle;
    private final CellStyle wrapStyle;

    private final CellStyle alternateStyle;

    private CellStyle evenRowStyle;

    private CellStyle oddRowStyle;

    private CellStyle highPriorityStyle;

    private CellStyle mediumPriorityStyle;

    private CellStyle lowPriorityStyle;

    private CellStyle summaryTitleStyle;

    private CellStyle summaryLabelStyle;

    private CellStyle summaryValueStyle;

    public ExcelStyleFactory(XSSFWorkbook workbook) {

        this.workbook = workbook;

        titleStyle = createTitleStyle();

        headerStyle = createHeaderStyle();

        normalStyle = createNormalStyle();

        wrapStyle = createWrapStyle();

        highPriorityStyle = createPriorityStyle(
                IndexedColors.RED);

        mediumPriorityStyle = createPriorityStyle(
                IndexedColors.ORANGE);

        lowPriorityStyle = createPriorityStyle(
                IndexedColors.LIGHT_GREEN);

        alternateStyle = createAlternateStyle();

        evenRowStyle = createEvenRowStyle();

        oddRowStyle = createOddRowStyle();

        highPriorityStyle = createPriorityStyle(
                IndexedColors.RED);

        mediumPriorityStyle = createPriorityStyle(
                IndexedColors.ORANGE);

        lowPriorityStyle = createPriorityStyle(
                IndexedColors.GREEN);

        summaryTitleStyle = createSummaryTitleStyle();

        summaryLabelStyle = createSummaryLabelStyle();

        summaryValueStyle = createSummaryValueStyle();

    }

    private CellStyle createOddRowStyle() {

        CellStyle style = workbook.createCellStyle();

        style.cloneStyleFrom(wrapStyle);

        return style;

    }

    private CellStyle createEvenRowStyle() {

        CellStyle style = workbook.createCellStyle();

        style.cloneStyleFrom(wrapStyle);

        style.setFillForegroundColor(
                IndexedColors.GREY_25_PERCENT.getIndex());

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);

        return style;

    }

    private CellStyle createTitleStyle() {

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();

        font.setBold(true);

        font.setFontHeightInPoints((short) 20);

        font.setFontName("Calibri");

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);

        style.setVerticalAlignment(
                VerticalAlignment.CENTER);

        return style;

    }

    private CellStyle createHeaderStyle() {

        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex());

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();

        font.setBold(true);

        font.setColor(
                IndexedColors.WHITE.getIndex());

        font.setFontHeightInPoints((short) 11);

        style.setFont(font);

        style.setAlignment(
                HorizontalAlignment.CENTER);

        style.setVerticalAlignment(
                VerticalAlignment.CENTER);

        setBorder(style);

        return style;

    }

    private CellStyle createNormalStyle() {

        CellStyle style = workbook.createCellStyle();

        style.setVerticalAlignment(
                VerticalAlignment.TOP);

        setBorder(style);

        return style;

    }

    private CellStyle createWrapStyle() {

        CellStyle style = workbook.createCellStyle();

        style.cloneStyleFrom(normalStyle);

        style.setWrapText(true);

        return style;

    }

    private CellStyle createPriorityStyle(

            IndexedColors color) {

        CellStyle style = workbook.createCellStyle();

        style.cloneStyleFrom(wrapStyle);

        style.setFillForegroundColor(
                color.getIndex());

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();

        font.setBold(true);

        font.setColor(
                IndexedColors.WHITE.getIndex());

        style.setFont(font);

        style.setAlignment(
                HorizontalAlignment.CENTER);

        return style;

    }

    private CellStyle createAlternateStyle() {

        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(
                IndexedColors.GREY_25_PERCENT.getIndex());

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);

        setBorder(style);

        return style;

    }

    private void setBorder(CellStyle style) {

        style.setBorderTop(BorderStyle.THIN);

        style.setBorderBottom(BorderStyle.THIN);

        style.setBorderLeft(BorderStyle.THIN);

        style.setBorderRight(BorderStyle.THIN);

    }

    public CellStyle getEvenRowStyle() {

        return evenRowStyle;

    }

    public CellStyle getOddRowStyle() {

        return oddRowStyle;

    }

    public CellStyle getHighPriorityStyle() {

        return highPriorityStyle;

    }

    public CellStyle getMediumPriorityStyle() {

        return mediumPriorityStyle;

    }

    public CellStyle getLowPriorityStyle() {

        return lowPriorityStyle;

    }

    private CellStyle createSummaryTitleStyle() {

    CellStyle style = workbook.createCellStyle();

    style.setFillForegroundColor(
            IndexedColors.DARK_BLUE.getIndex());

    style.setFillPattern(
            FillPatternType.SOLID_FOREGROUND);

    Font font = workbook.createFont();

    font.setBold(true);

    font.setColor(
            IndexedColors.WHITE.getIndex());

    font.setFontHeightInPoints((short)14);

    style.setFont(font);

    style.setAlignment(
            HorizontalAlignment.CENTER);

    return style;

}

private CellStyle createSummaryLabelStyle() {

    CellStyle style = workbook.createCellStyle();

    style.cloneStyleFrom(wrapStyle);

    Font font = workbook.createFont();

    font.setBold(true);

    style.setFont(font);

    return style;

}

private CellStyle createSummaryValueStyle() {

    CellStyle style = workbook.createCellStyle();

    style.cloneStyleFrom(wrapStyle);

    return style;

}

public CellStyle getSummaryTitleStyle() {
    return summaryTitleStyle;
}

public CellStyle getSummaryLabelStyle() {
    return summaryLabelStyle;
}

public CellStyle getSummaryValueStyle() {
    return summaryValueStyle;
}

}