package com.genai.ollamarestapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "generation_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String storyKey;
    
    private String storySummary;

    @Column(columnDefinition = "TEXT")
    private String acceptanceCriteria;

    private String generationType;

    private String aiModel;

    private Long responseTimeMs;

    private Integer testCaseCount;

    private String status;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "history",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<GenerationTestCase> testCases = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void addTestCase(GenerationTestCase tc) {
        tc.setHistory(this);
        testCases.add(tc);
    }
}