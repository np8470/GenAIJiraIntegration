package com.genai.ollamarestapi.export.pdf;

import com.genai.ollamarestapi.export.common.ReportContext;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfExporter {

    public byte[] export(

            ReportContext context)

            throws Exception {

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        Document document =
                new Document(
                        PageSize.A4,
                        40,
                        40,
                        60,
                        50);

        PdfWriter writer =
                PdfWriter.getInstance(
                        document,
                        out);

        writer.setPageEvent(
        new PdfPageEvent());

        document.open();

        /* PdfHeader.create(
                document,
                context.getMetadata().getStoryKey(),
                context.getTestCases()); */
                PdfHeader.create(
        document,
        context);

        PdfTestCaseWriter.write(
                document,
                context.getTestCases());


        /* PdfSummaryBuilder.build(
                document,
                context.getMetadata().getStoryKey(),
                context.getTestCases()); */
                PdfSummaryBuilder.build(
        document,
        context);

        document.close();

        return out.toByteArray();

    }

}