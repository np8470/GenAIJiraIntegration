package com.genai.ollamarestapi.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.dto.DashboardStatisticsResponse;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.repository.DashboardRepository;
import com.genai.ollamarestapi.repository.GenerationHistoryRepository;
import com.genai.ollamarestapi.repository.GenerationTestCaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

        private final DashboardRepository dashboardRepository;

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

                long totalTestCases = dashboardRepository.totalTestCases(user);

                double averageResponse = dashboardRepository.averageResponse(user);

                long uploaded = generationTestCaseRepository.countUploaded();

                List<String> models = dashboardRepository.findMostUsedModels(
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

                                .mostUsedAiModel(aiModel == null ? "-" : aiModel)

                                .build();

        }

}