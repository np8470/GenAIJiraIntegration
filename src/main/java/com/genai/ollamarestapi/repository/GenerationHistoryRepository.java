package com.genai.ollamarestapi.repository;

import com.genai.ollamarestapi.entity.GenerationHistory;
import com.genai.ollamarestapi.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenerationHistoryRepository
                extends JpaRepository<GenerationHistory, Long> {

        Page<GenerationHistory> findByUserOrderByCreatedAtDesc(
                        User user,
                        Pageable pageable);

        Optional<GenerationHistory> findByIdAndUser(
                        Long id,
                        User user);

        long countByUser(User user);

        long countByUserAndStatus(
                        User user,
                        String status);

        // ----------------------------------------------------
        // Dashboard
        // ----------------------------------------------------

        @Query("""
                        select coalesce(sum(g.testCaseCount),0)
                        from GenerationHistory g
                        where g.user=:user
                        """)
        long totalGeneratedTestCases(User user);

        @Query("""
                        select coalesce(avg(g.responseTimeMs),0)
                        from GenerationHistory g
                        where g.user=:user
                        """)
        Double averageResponse(User user);

        @Query("""
                        select g.aiModel

                        from GenerationHistory g

                        where g.user=:user

                        group by g.aiModel

                        order by count(g) desc
                        """)
        List<String> findMostUsedModels(

                        User user,

                        Pageable pageable);

        List<GenerationHistory>

                        findTop10ByUserOrderByCreatedAtDesc(User user);

        @Query("""

                        select count(g)

                        from GenerationHistory g

                        where g.user=:user

                        and g.generationType='UI'

                        """)
        long countUiReports(User user);

        @Query("""

                        select count(g)

                        from GenerationHistory g

                        where g.user=:user

                        and g.generationType='API'

                        """)
        long countApiReports(User user);

        @Query("""

                        select count(g)

                        from GenerationHistory g

                        where g.user=:user

                        and g.generationType='SELENIUM'

                        """)
        long countSeleniumReports(User user);


        long countByUserAndGenerationType(User user, String generationType);

}