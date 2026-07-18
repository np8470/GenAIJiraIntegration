package com.genai.ollamarestapi.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentGenerationDto {

    private Long id;

    private String storyKey;

    private String generationType;

    private String aiModel;

    private Integer testCaseCount;

    private String status;

    private LocalDateTime createdAt;

}
