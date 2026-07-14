package com.genai.ollamarestapi.export;

import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xwpf.usermodel.*;

public final class WordHyperlinkHelper {

    private WordHyperlinkHelper() {
    }

    public static void add(

            XWPFDocument document,

            XWPFParagraph paragraph,

            String text,

            String url) {

        PackageRelationship relationship =
                document.getPackagePart()
                        .addExternalRelationship(
                                url,
                                XWPFRelation.HYPERLINK.getRelation());

        String id = relationship.getId();

        XWPFHyperlinkRun hyperlink =
                paragraph.createHyperlinkRun(id);

        hyperlink.setText(text);

        hyperlink.setColor("0563C1");

        hyperlink.setUnderline(
                UnderlinePatterns.SINGLE);

        hyperlink.setBold(true);
    }

}