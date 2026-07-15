package com.genai.ollamarestapi.export.word;

import org.apache.poi.xwpf.usermodel.*;

public final class WordStyles {

    private WordStyles() {
    }

    public static void title(XWPFRun run) {

        run.setBold(true);
        run.setFontSize(22);
        run.setFontFamily("Calibri");
        run.setColor(WordTheme.PRIMARY);

    }

    public static void heading(XWPFRun run) {

        run.setBold(true);
        run.setFontSize(16);
        run.setFontFamily("Calibri");
        run.setColor(WordTheme.HEADER);

    }

    public static void normal(XWPFRun run) {

        run.setFontFamily("Calibri");
        run.setFontSize(11);

    }

    public static void priorityHigh(XWPFRun run) {

        normal(run);

        run.setBold(true);
        run.setColor(WordTheme.HIGH);

    }

    public static void priorityMedium(XWPFRun run) {

        normal(run);

        run.setBold(true);
        run.setColor(WordTheme.MEDIUM);

    }

    public static void priorityLow(XWPFRun run) {

        normal(run);

        run.setBold(true);
        run.setColor(WordTheme.LOW);

    }

}