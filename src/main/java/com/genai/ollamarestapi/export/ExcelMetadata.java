package com.genai.ollamarestapi.export;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelMetadata {

    private ExcelMetadata() {
    }

    public static void apply(
            XSSFWorkbook workbook,
            String storyKey){

        POIXMLProperties.CoreProperties props =
                workbook.getProperties()
                        .getCoreProperties();

        props.setCreator(
                ExcelConstants.AUTHOR);

        props.setTitle(
                storyKey + " Test Case Report");

        props.setSubjectProperty(
                ExcelConstants.SUBJECT);

        props.setDescription(
                "Generated using TestPilot AI");

    }

}