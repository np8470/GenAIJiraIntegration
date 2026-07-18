package com.genai.ollamarestapi.repository;

import com.genai.ollamarestapi.entity.GenerationTestCase;
import com.genai.ollamarestapi.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenerationTestCaseRepository
                extends JpaRepository<GenerationTestCase, Long> {

        /* @Query("""
                        SELECT COUNT(g)
                        FROM GenerationTestCase g
                        WHERE g.uploadedToJira=true
                        """)
        long countUploaded(); */

        Optional<GenerationTestCase> findById(Long id);

        long countByHistoryUserAndPriorityIgnoreCase(

                        User user,

                        String priority);

        long countByHistoryUserAndTypeIgnoreCase(

                        User user,

                        String type);

        long countByHistoryUserAndUploadedToJiraTrue(

                        User user);

        long countByHistoryUserAndUploadedToJiraFalse(

                        User user);

}