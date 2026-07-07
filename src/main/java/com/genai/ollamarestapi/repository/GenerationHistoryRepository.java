package com.genai.ollamarestapi.repository;

import com.genai.ollamarestapi.entity.GenerationHistory;
import com.genai.ollamarestapi.entity.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

}