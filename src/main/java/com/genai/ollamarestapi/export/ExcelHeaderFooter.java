package com.genai.ollamarestapi.export;

import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExcelHeaderFooter {

    private ExcelHeaderFooter() {
    }

    public static void apply(

            Sheet sheet,

            String storyKey) {

        Header header = sheet.getHeader();

        header.setLeft(

                "TestPilot AI Enterprise");

        header.setCenter(

                "Generated Test Cases");

        header.setRight(

                "Story : " + storyKey);

        Footer footer = sheet.getFooter();

        footer.setLeft(

                "Generated : " +

                        LocalDateTime.now()

                                .format(

                                        DateTimeFormatter.ofPattern(

                                                "dd-MMM-yyyy HH:mm")));

        footer.setCenter(

                "Page &P of &N");

        footer.setRight(

                "© TestPilot AI");

    }

}