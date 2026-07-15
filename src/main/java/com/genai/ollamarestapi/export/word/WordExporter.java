package com.genai.ollamarestapi.export.word;

import com.genai.ollamarestapi.export.common.ReportContext;
import com.genai.ollamarestapi.model.ai.TestCase;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class WordExporter {

    public byte[] export(ReportContext context)

            throws Exception {

        XWPFDocument document = new XWPFDocument();
        
        boolean landscape =

                context.getTestCases().stream()

                        .anyMatch(tc ->

                        tc.getSteps() != null

                                && tc.getSteps().size() > 8);

        if (landscape) {

            WordPageSetup.configureLandscape(document);

        } else {

            WordPageSetup.configurePortrait(document);

        }
        WordHeaderFooter.apply(document, context.getMetadata().getStoryKey());

        WordWatermark.add(document);

        WordHeader.create(
        document,
        context);

        WordTestCaseWriter.write(

                document,

                context.getTestCases());

        WordSummaryBuilder.create(
        document,
        context);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        document.write(out);

        document.close();

        return out.toByteArray();

    }

}