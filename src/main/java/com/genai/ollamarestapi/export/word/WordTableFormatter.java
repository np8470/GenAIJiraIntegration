package com.genai.ollamarestapi.export.word;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;

public final class WordTableFormatter {

    private WordTableFormatter(){}

    public static void format(

            XWPFTable table){

        table.setWidth("100%");

        table.setInsideHBorder(

                XWPFBorderType.SINGLE,

                4,

                0,

                "CFCFCF");

        table.setInsideVBorder(

                XWPFBorderType.SINGLE,

                4,

                0,

                "CFCFCF");

        table.setTopBorder(

                XWPFBorderType.SINGLE,

                6,

                0,

                WordTheme.HEADER);

        table.setBottomBorder(

                XWPFBorderType.SINGLE,

                6,

                0,

                WordTheme.HEADER);

    }

}