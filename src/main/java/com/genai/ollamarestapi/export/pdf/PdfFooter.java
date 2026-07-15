package com.genai.ollamarestapi.export.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class PdfFooter extends PdfPageEventHelper {

    @Override
    public void onEndPage(
            PdfWriter writer,
            Document document) {

        PdfContentByte cb =
                writer.getDirectContent();

        Phrase footer =
                new Phrase(
                        PdfConstants.GENERATED_BY
                                + " | Page "
                                + writer.getPageNumber(),
                        PdfStyles.SMALL);

        ColumnText.showTextAligned(

                cb,

                Element.ALIGN_CENTER,

                footer,

                (document.left() + document.right()) / 2,

                document.bottom() - 18,

                0);

    }

}