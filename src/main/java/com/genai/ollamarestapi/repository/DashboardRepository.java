package com.genai.ollamarestapi.repository;

import com.genai.ollamarestapi.entity.GenerationHistory;
import com.genai.ollamarestapi.entity.User;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DashboardRepository
        extends JpaRepository<GenerationHistory, Long> {

    @Query("""
            SELECT COALESCE(SUM(g.testCaseCount),0)
            FROM GenerationHistory g
            WHERE g.user = :user
            """)
    Long totalTestCases(User user);

    @Query("""
            SELECT COALESCE(AVG(g.responseTimeMs),0)
            FROM GenerationHistory g
            WHERE g.user=:user
            """)
    Double averageResponse(User user);

    @Query("""
            SELECT g.aiModel
            FROM GenerationHistory g
            WHERE g.user = :user
            GROUP BY g.aiModel
            ORDER BY COUNT(g) DESC
            """)
    List<String> findMostUsedModels(User user, Pageable pageable);

}