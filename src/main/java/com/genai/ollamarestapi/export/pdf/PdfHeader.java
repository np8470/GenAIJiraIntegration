package com.genai.ollamarestapi.export.pdf;

import com.genai.ollamarestapi.export.common.ReportContext;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.lowagie.text.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfHeader {

    private PdfHeader() {
    }

    public static void create(
        Document document,
        ReportContext context) throws Exception {

        Paragraph company = new Paragraph(
                PdfConstants.COMPANY,
                PdfStyles.TITLE);

        company.setAlignment(Element.ALIGN_CENTER);

        document.add(company);

        Paragraph subtitle = new Paragraph(
                PdfConstants.PRODUCT,
                PdfStyles.SUBTITLE);

        subtitle.setAlignment(Element.ALIGN_CENTER);

        document.add(subtitle);

        document.add(Chunk.NEWLINE);

        Anchor anchor = new Anchor(
                context.getMetadata().getStoryKey(),
                PdfStyles.NORMAL);

        anchor.setReference(

                "https://YOURDOMAIN.atlassian.net/browse/"

                        + context.getMetadata().getStoryKey());

        Paragraph p = new Paragraph();

        p.add("Story Key : ");

        p.add(anchor);

        document.add(p);

        document.add(

                new Paragraph(

                        "Generated : "

                                + LocalDateTime.now()

                                        .format(

                                                DateTimeFormatter.ofPattern(

                                                        "dd-MMM-yyyy hh:mm a")),

                        PdfStyles.NORMAL));

        document.add(

                new Paragraph(

                        "Total Test Cases : "

                                + context.getStatistics().getTotal(),

                        PdfStyles.NORMAL));

        document.add(

                new Paragraph(

                        PdfConstants.VERSION,

                        PdfStyles.NORMAL));

        document.add(Chunk.NEWLINE);

    }

}