package com.genai.ollamarestapi.export;

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

            String storyKey,

            List<TestCase> testCases)

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

        PdfHeader.create(
                document,
                storyKey,
                testCases);

        PdfTestCaseWriter.write(
                document,
                testCases);


        PdfSummaryBuilder.build(
                document,
                storyKey,
                testCases);

        document.close();

        return out.toByteArray();

    }

}