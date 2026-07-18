package com.genai.ollamarestapi.service;

import com.genai.ollamarestapi.constants.AiConstants;
import com.genai.ollamarestapi.dto.GenerationHistoryDetailResponse;
import com.genai.ollamarestapi.dto.GenerationHistoryResponse;
import com.genai.ollamarestapi.dto.GenerationTestCaseResponse;
import com.genai.ollamarestapi.entity.GenerationHistory;
import com.genai.ollamarestapi.entity.GenerationTestCase;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.model.ai.TestCase;
import com.genai.ollamarestapi.repository.GenerationHistoryRepository;
import com.genai.ollamarestapi.repository.GenerationTestCaseRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerationHistoryService {

    private final GenerationHistoryRepository repository;

    private final GenerationTestCaseRepository testCaseRepository;

    public GenerationHistory save(GenerationHistory history) {
        return repository.save(history);
    }

    public Page<GenerationHistoryResponse> getHistory(
            User user,
            Pageable pageable) {

        return repository.findByUserOrderByCreatedAtDesc(user, pageable)
                .map(this::convert);
    }

    private GenerationHistoryResponse convert(
            GenerationHistory entity) {

        return GenerationHistoryResponse.builder()
                .id(entity.getId())
                .storyKey(entity.getStoryKey())
                .storySummary(entity.getStorySummary())
                .testCaseCount(entity.getTestCaseCount())
                .aiModel(entity.getAiModel())
                .responseTimeMs(entity.getResponseTimeMs())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public GenerationHistoryDetailResponse getHistoryDetail(
            Long id,
            User user) {

        GenerationHistory history = repository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("History not found"));

        return convertDetail(history);
    }

    private GenerationHistoryDetailResponse convertDetail(
            GenerationHistory history) {

        return GenerationHistoryDetailResponse.builder()

                .id(history.getId())
                .storyKey(history.getStoryKey())
                .acceptanceCriteria(history.getAcceptanceCriteria())
                .generationType(history.getGenerationType())
                .aiModel(history.getAiModel())
                .responseTimeMs(history.getResponseTimeMs())
                .status(history.getStatus())
                .testCaseCount(history.getTestCaseCount())
                .createdAt(history.getCreatedAt())

                .testCases(

                        history.getTestCases()

                                .stream()

                                .map(tc ->

                                GenerationTestCaseResponse.builder()

                                        .id(tc.getId())
                                        .title(tc.getTitle())
                                        .description(tc.getDescription())
                                        .priority(tc.getPriority())
                                        .type(tc.getType())
                                        .precondition(tc.getPrecondition())
                                        .steps(tc.getSteps())
                                        .expectedResult(tc.getExpectedResult())
                                        .uploadedToJira(tc.getUploadedToJira())
                                        .jiraTestcaseKey(tc.getJiraTestcaseKey())

                                        .build())

                                .toList())

                .build();
    }

    public void delete(
            Long id,
            User user) {

        GenerationHistory history = repository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("History not found"));

        repository.delete(history);
    }

    public GenerationHistory saveGeneration(
            User user,
            String storyKey,
            String acceptanceCriteria,
            GenerationType generationType,
            List<TestCase> testCases,
            long responseTime) {

        GenerationHistory history = GenerationHistory.builder()
                .user(user)
                .storyKey(storyKey)
                .acceptanceCriteria(acceptanceCriteria)
                .generationType(generationType.name())
                .aiModel(AiConstants.DEFAULT_MODEL)
                .responseTimeMs(responseTime)
                .testCaseCount(testCases.size())
                .status("SUCCESS")
                .build();

        for (TestCase tc : testCases) {

            GenerationTestCase entity = GenerationTestCase.builder()
                    .title(tc.getTitle())
                    .description(tc.getDescription())
                    .priority(tc.getPriority())
                    .type(tc.getType())
                    .precondition(tc.getPrecondition())
                    .steps(
                            tc.getSteps() == null
                                    ? ""
                                    : String.join("\n",
                                            tc.getSteps()))
                    .expectedResult(tc.getExpectedResult())
                    .uploadedToJira(false)
                    .build();

            history.addTestCase(entity);
        }

        return repository.save(history);
    }

    public void saveFailure(
        User user,
        String storyKey,
        GenerationType generationType,
        long responseTime) {

    GenerationHistory history = GenerationHistory.builder()
            .user(user)
            .storyKey(storyKey)
            .generationType(generationType.name())
            .status("FAILED")
            .responseTimeMs(responseTime)
            .build();

    repository.save(history);
}

    @Transactional
public void markUploaded(
        Long testCaseId,
        String jiraKey){

    GenerationTestCase tc =
            testCaseRepository.findById(testCaseId)
                    .orElseThrow();

    tc.setUploadedToJira(true);

    tc.setJiraTestcaseKey(jiraKey);

}

}