package com.genai.ollamarestapi.repository;

import com.genai.ollamarestapi.entity.GenerationTestCase;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenerationTestCaseRepository
                extends JpaRepository<GenerationTestCase, Long> {

        @Query("""
                        SELECT COUNT(g)
                        FROM GenerationTestCase g
                        WHERE g.uploadedToJira=true
                        """)
        long countUploaded();

        Optional<GenerationTestCase> findById(Long id);

}