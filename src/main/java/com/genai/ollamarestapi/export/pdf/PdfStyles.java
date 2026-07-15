package com.genai.ollamarestapi.export.pdf;

import java.awt.Color;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

public final class PdfStyles {

    private PdfStyles() {
    }

    public static final Font TITLE =
            FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    22,
                    Color.decode("#0d6efd"));

    public static final Font SUBTITLE =
            FontFactory.getFont(
                    FontFactory.HELVETICA,
                    12,
                    Color.GRAY);

    public static final Font SECTION =
            FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    16,
                    Color.decode("#0d6efd"));

    public static final Font NORMAL =
            FontFactory.getFont(
                    FontFactory.HELVETICA,
                    10,
                    Color.BLACK);

    public static final Font BOLD =
            FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10,
                    Color.BLACK);

    public static final Font SMALL =
            FontFactory.getFont(
                    FontFactory.HELVETICA,
                    9,
                    Color.GRAY);

    public static final Font HIGH =
            FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10,
                    Color.RED);

    public static final Font MEDIUM =
            FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10,
                    new Color(255,140,0));

    public static final Font LOW =
            FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10,
                    new Color(0,130,0));

}