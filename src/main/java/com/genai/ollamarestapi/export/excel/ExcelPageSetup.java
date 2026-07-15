package com.genai.ollamarestapi.export.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public final class ExcelPageSetup {

    private ExcelPageSetup() {
    }

    public static void configure(
            Workbook workbook,
            Sheet sheet,
            int lastRow) {

        PrintSetup printSetup = sheet.getPrintSetup();

        printSetup.setLandscape(true);

        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);

        printSetup.setFitWidth((short)1);

        printSetup.setFitHeight((short)0);

        sheet.setFitToPage(true);

        sheet.setHorizontallyCenter(true);

        sheet.setMargin(Sheet.LeftMargin,0.3);

        sheet.setMargin(Sheet.RightMargin,0.3);

        sheet.setMargin(Sheet.TopMargin,0.5);

        sheet.setMargin(Sheet.BottomMargin,0.5);

        sheet.setRepeatingRows(
        CellRangeAddress.valueOf("8:8"));

    }

}