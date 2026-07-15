package com.genai.ollamarestapi.export.pdf;

import com.lowagie.text.*;

import java.util.List;

public class PdfTableOfContents {

    public static void build(

            Document document,

            List<String> titles)

            throws Exception{

        document.newPage();

        document.add(

                new Paragraph(

                        "TABLE OF CONTENTS",

                        PdfStyles.SECTION));

        document.add(Chunk.NEWLINE);

        int page=2;

        for(String title : titles){

            document.add(

                    new Paragraph(

                            title

                            + " ........ "

                            + page++));

        }

    }

}