package com.genai.ollamarestapi.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.dto.DashboardChartResponse;
import com.genai.ollamarestapi.dto.DashboardStatisticsResponse;
import com.genai.ollamarestapi.dto.RecentGenerationDto;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.repository.GenerationHistoryRepository;
import com.genai.ollamarestapi.repository.GenerationTestCaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

        private final GenerationTestCaseRepository generationTestCaseRepository;
        private final GenerationHistoryRepository generationHistoryRepository;

        public DashboardStatisticsResponse statistics(User user) {

                long totalGenerations = generationHistoryRepository.countByUser(user);

                long success = generationHistoryRepository.countByUserAndStatus(
                                user,
                                "SUCCESS");

                long failed = generationHistoryRepository.countByUserAndStatus(
                                user,
                                "FAILED");

                long totalTestCases = generationHistoryRepository.totalGeneratedTestCases(user);

                double averageResponse = generationHistoryRepository.averageResponse(user);

                // long uploaded = generationTestCaseRepository.countUploaded();
                long uploaded = generationTestCaseRepository.countByHistoryUserAndUploadedToJiraTrue(user);

                long pending = generationTestCaseRepository.countByHistoryUserAndUploadedToJiraFalse(user);

                List<String> models = generationHistoryRepository.findMostUsedModels(
                                user,
                                PageRequest.of(0, 1));

                String aiModel = models.isEmpty()
                                ? "-"
                                : models.get(0);

                double successRate = 0;

                if (totalGenerations > 0) {

                        successRate = ((double) success / totalGenerations) * 100;
                }

                return DashboardStatisticsResponse.builder()

                                .totalGenerations(totalGenerations)

                                .successfulRuns(success)

                                .failedRuns(failed)

                                .successRate(successRate)

                                .totalTestCases(totalTestCases)

                                .averageResponseTime(averageResponse)

                                .uploadedToJira(uploaded)

                                .pendingUpload(pending)

                                .mostUsedAiModel(aiModel == null ? "-" : aiModel)

                                .uiReports(
                                                generationHistoryRepository.countUiReports(user))

                                .apiReports(
                                                generationHistoryRepository.countApiReports(user))

                                .seleniumReports(
                                                generationHistoryRepository.countSeleniumReports(user))

                                .highPriority(
                                                generationTestCaseRepository.countByHistoryUserAndPriorityIgnoreCase(
                                                                user,
                                                                "HIGH"))

                                .mediumPriority(
                                                generationTestCaseRepository.countByHistoryUserAndPriorityIgnoreCase(
                                                                user,
                                                                "MEDIUM"))

                                .lowPriority(
                                                generationTestCaseRepository.countByHistoryUserAndPriorityIgnoreCase(
                                                                user,
                                                                "LOW"))

                                .build();

        }

        public List<RecentGenerationDto> recentGenerations(User user) {

                return generationHistoryRepository

                                .findTop10ByUserOrderByCreatedAtDesc(user)

                                .stream()

                                .map(history ->

                                RecentGenerationDto.builder()

                                                .id(history.getId())

                                                .storyKey(history.getStoryKey())

                                                .generationType(history.getGenerationType())

                                                .aiModel(history.getAiModel())

                                                .status(history.getStatus())

                                                .testCaseCount(history.getTestCaseCount())

                                                .createdAt(history.getCreatedAt())

                                                .build())

                                .toList();

        }

        public DashboardChartResponse chartData(User user) {

                return DashboardChartResponse.builder()

                                .highPriority(

                                                generationTestCaseRepository

                                                                .countByHistoryUserAndPriorityIgnoreCase(

                                                                                user,

                                                                                "HIGH"))

                                .mediumPriority(

                                                generationTestCaseRepository

                                                                .countByHistoryUserAndPriorityIgnoreCase(

                                                                                user,

                                                                                "MEDIUM"))

                                .lowPriority(

                                                generationTestCaseRepository

                                                                .countByHistoryUserAndPriorityIgnoreCase(

                                                                                user,

                                                                                "LOW"))

                                .ui(

                                                generationHistoryRepository

                                                                .countByUserAndGenerationType(

                                                                                user,

                                                                                GenerationType.UI_TEST_CASES.name()))

                                .api(

                                                generationHistoryRepository

                                                                .countByUserAndGenerationType(

                                                                                user,

                                                                                GenerationType.API_TEST_CASES.name()))

                                .selenium(

                                                generationHistoryRepository

                                                                .countByUserAndGenerationType(

                                                                                user,

                                                                                GenerationType.SELENIUM_SCRIPT.name()))

                                .build();

        }

}