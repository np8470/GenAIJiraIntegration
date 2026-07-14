package com.genai.ollamarestapi.export;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class PdfFonts {

    public static Font normal()
            throws Exception{

        BaseFont bf =
                BaseFont.createFont(

                        "fonts/calibri.ttf",

                        BaseFont.IDENTITY_H,

                        BaseFont.EMBEDDED);

        return new Font(
                bf,
                10);

    }

}