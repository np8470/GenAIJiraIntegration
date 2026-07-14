package com.genai.ollamarestapi.export;

import com.lowagie.text.Document;
import com.lowagie.text.Image;

public class PdfLogo {

    private PdfLogo(){}

    public static void add(
            Document document)
            throws Exception{

        Image logo =
                Image.getInstance(

                        PdfLogo.class

                                .getResource("/logo.png"));

        logo.scalePercent(35);

        logo.setAlignment(Image.ALIGN_CENTER);

        document.add(logo);

    }

}