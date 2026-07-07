package com.genai.ollamarestapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "generation_test_case")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private GenerationHistory history;

    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    private String priority;

    private String type;

    @Column(columnDefinition="TEXT")
    private String precondition;

    @Column(columnDefinition="TEXT")
    private String steps;

    @Column(columnDefinition="TEXT")
    private String expectedResult;

    private Boolean uploadedToJira;

    private String jiraTestcaseKey;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (uploadedToJira == null) {
            uploadedToJira = false;
        }
    }
}