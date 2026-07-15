package com.genai.ollamarestapi.export.word;

import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

public final class WordPageSetup {

    private WordPageSetup() {
    }

    //----------------------------------------
    // Portrait
    //----------------------------------------

    public static void configurePortrait(

            XWPFDocument document) {

        CTSectPr sectPr =

                document.getDocument()

                        .getBody()

                        .addNewSectPr();

        CTPageSz pageSize =

                sectPr.addNewPgSz();

        pageSize.setOrient(

                STPageOrientation.PORTRAIT);

        pageSize.setW(

                BigInteger.valueOf(11906));

        pageSize.setH(

                BigInteger.valueOf(16838));

        configureMargins(sectPr);

    }

    //----------------------------------------
    // Landscape
    //----------------------------------------

    public static void configureLandscape(

            XWPFDocument document) {

        CTSectPr sectPr =

                document.getDocument()

                        .getBody()

                        .addNewSectPr();

        CTPageSz pageSize =

                sectPr.addNewPgSz();

        pageSize.setOrient(

                STPageOrientation.LANDSCAPE);

        pageSize.setW(

                BigInteger.valueOf(16838));

        pageSize.setH(

                BigInteger.valueOf(11906));

        configureMargins(sectPr);

    }

    //----------------------------------------

    private static void configureMargins(

            CTSectPr sectPr) {

        CTPageMar mar =

                sectPr.addNewPgMar();

        mar.setTop(

                BigInteger.valueOf(720));

        mar.setBottom(

                BigInteger.valueOf(720));

        mar.setLeft(

                BigInteger.valueOf(720));

        mar.setRight(

                BigInteger.valueOf(720));

    }

}