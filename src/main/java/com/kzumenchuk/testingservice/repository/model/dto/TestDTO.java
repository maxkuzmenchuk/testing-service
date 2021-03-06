package com.kzumenchuk.testingservice.repository.model.dto;

import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDTO {
    private Long testID;
    private String title;
    private String description;
    private String category;
    private LocalDate creatingDate;
    private LocalDateTime updatingDate;
    private Long creatorID;
    private boolean isArchived;
    private Set<QuestionEntity> questions;
    private Set<TagEntity> tags;
}
