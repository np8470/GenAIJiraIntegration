package com.genai.ollamarestapi.export;

import com.genai.ollamarestapi.model.ai.TestCase;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class WordExporter {

    public byte[] export(

            String storyKey,

            List<TestCase> testCases)

            throws Exception {

        XWPFDocument document = new XWPFDocument();
        
        boolean landscape =

                testCases.stream()

                        .anyMatch(tc ->

                        tc.getSteps() != null

                                && tc.getSteps().size() > 8);

        if (landscape) {

            WordPageSetup.configureLandscape(document);

        } else {

            WordPageSetup.configurePortrait(document);

        }
        WordHeaderFooter.apply(document, storyKey);

        WordWatermark.add(document);

        WordHeader.create(

                document,

                storyKey,

                testCases);

        WordTestCaseWriter.write(

                document,

                testCases);

        WordSummaryBuilder.create(

                document,

                storyKey,

                testCases);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        document.write(out);

        document.close();

        return out.toByteArray();

    }

}