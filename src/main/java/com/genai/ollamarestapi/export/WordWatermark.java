package com.genai.ollamarestapi.export;

import org.apache.poi.xwpf.usermodel.*;

public final class WordWatermark {

    private WordWatermark(){}

    public static void add(

            XWPFDocument document){

        XWPFParagraph p =
                document.createParagraph();

        p.setAlignment(
                ParagraphAlignment.CENTER);

        XWPFRun run =
                p.createRun();

        run.setFontSize(42);

        run.setBold(true);

        run.setColor("DDDDDD");

        run.setText("CONFIDENTIAL");

        document.createParagraph();

    }

}