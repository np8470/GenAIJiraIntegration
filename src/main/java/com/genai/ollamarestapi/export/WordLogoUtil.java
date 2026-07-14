package com.genai.ollamarestapi.export;

import java.io.InputStream;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public final class WordLogoUtil {

    private WordLogoUtil() {
    }

    public static void addLogo(

            XWPFDocument document,

            XWPFParagraph paragraph,

            String resourcePath)

            throws Exception {

        InputStream stream =
                WordLogoUtil.class
                        .getResourceAsStream(resourcePath);

        if (stream == null) {
            return;
        }

        paragraph.setAlignment(
                ParagraphAlignment.CENTER);

        XWPFRun run =
                paragraph.createRun();

        run.addPicture(

                stream,

                Document.PICTURE_TYPE_PNG,

                "logo.png",

                Units.toEMU(120),

                Units.toEMU(120));

        stream.close();

    }

}