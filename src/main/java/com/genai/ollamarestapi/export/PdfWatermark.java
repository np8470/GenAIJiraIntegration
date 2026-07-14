package com.genai.ollamarestapi.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import java.awt.*;

public class PdfWatermark extends PdfPageEventHelper {

    @Override
    public void onEndPage(
            PdfWriter writer,
            Document document) {

        PdfContentByte canvas =
                writer.getDirectContentUnder();

        Font font =
                FontFactory.getFont(

                        FontFactory.HELVETICA_BOLD,

                        60,

                        Color.LIGHT_GRAY);

        Phrase watermark =
                new Phrase(
                        "CONFIDENTIAL",
                        font);

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_CENTER,

                watermark,

                300,

                400,

                45);

    }

}