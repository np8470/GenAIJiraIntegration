package com.genai.ollamarestapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.entity.GenerationTestCase;
import com.genai.ollamarestapi.model.UploadRequest;
import com.genai.ollamarestapi.model.UploadResponse;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.genai.ollamarestapi.repository.GenerationTestCaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraUploadService {

    private final JiraService jiraService;

    private final GenerationTestCaseRepository generationTestCaseRepository;

    private final GenerationHistoryService generationHistoryService;

    /* public UploadResponse upload(

            UploadRequest request) {

        int uploaded = 0;

        int failed = 0;

        List<String> links = new ArrayList<>();

        List<GenerationTestCase> entities =
            generationTestCaseRepository.findAllByIdIn(request.getTestCaseIds());


        //for(TestCase tc : request.getTestCases()){
        for(Long id : request.getTestCaseIds()){

            try{

                GenerationTestCase entity =
            generationTestCaseRepository
                    .findById(id)
                    .orElseThrow();

    TestCase tc = convertToTestCase(entity);

                String key =

                        jiraService.createTestCase(

                                "SCRUM",

                                tc);

                jiraService.linkIssue(

                        request.getStoryKey(),

                        key);

                        generationHistoryService.markUploaded(
            entity.getId(),
            key);

                links.add(

                        jiraService.getBrowseUrl(key));

                uploaded++;

            }

            catch(Exception ex){

                failed++;

                log.error("Upload failed",ex);

            }

        }

        return new UploadResponse(

                uploaded>0,

                uploaded+" uploaded.",

                uploaded,

                failed,

                links);

    } */

                public UploadResponse upload(UploadRequest request){

    int uploaded = 0;

    int failed = 0;

    List<String> links = new ArrayList<>();

    List<GenerationTestCase> entities =
            generationTestCaseRepository.findAllByIdIn(request.getTestCaseIds());

    for(GenerationTestCase entity : entities){

        try{
            
            TestCase tc = convertToTestCase(entity);
            String jiraKey =
                    jiraService.createTestCase(
                            "SCRUM",
                            tc);

            jiraService.linkIssue(
                    request.getStoryKey(),
                    jiraKey);

            generationHistoryService.markUploaded(
        entity.getId(),
        jiraKey);

            links.add(
                    jiraService.getBrowseUrl(jiraKey));

            uploaded++;

        }
        catch(Exception ex){

            failed++;

            log.error("Upload failed",ex);

        }

    }

    return new UploadResponse(

            uploaded>0,

            uploaded+" uploaded.",

            uploaded,

            failed,

            links);

}


    private String buildJiraDescription(TestCase tc) {

    StringBuilder sb = new StringBuilder();

    sb.append("Description\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getDescription()));

    sb.append("\n\nPriority\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getPriority()));

    sb.append("\n\nType\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getType()));

    sb.append("\n\nPrecondition\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getPrecondition()));

    sb.append("\n\nSteps\n");
    sb.append("-------------------------\n");

    if (tc.getSteps() != null) {

        int i = 1;

        for (String step : tc.getSteps()) {

            sb.append(i++)
                    .append(". ")
                    .append(step)
                    .append("\n");
        }
    }

    sb.append("\nExpected Result\n");
    sb.append("-------------------------\n");
    sb.append(nullToEmpty(tc.getExpectedResult()));

    return sb.toString();
}

private String nullToEmpty(String value) {

    return value == null ? "" : value;

}

private TestCase convertToTestCase(GenerationTestCase entity) {

    TestCase tc = new TestCase();
    tc.setId(entity.getId().toString());
    tc.setTitle(entity.getTitle());
    tc.setDescription(entity.getDescription());
    tc.setPriority(entity.getPriority());
    tc.setType(entity.getType());
    tc.setPrecondition(entity.getPrecondition());

    if (entity.getSteps() != null && !entity.getSteps().isBlank()) {
        tc.setSteps(List.of(entity.getSteps().split("\\n")));
    }

    tc.setExpectedResult(entity.getExpectedResult());

    return tc;
}

}