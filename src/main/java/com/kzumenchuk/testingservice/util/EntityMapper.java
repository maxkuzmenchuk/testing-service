package com.kzumenchuk.testingservice.util;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.repository.model.dto.TestResultDTO;

public class EntityMapper {
    public static TestEntity fromDTOToEntity(TestDTO modelDTO) {
        return TestEntity.builder()
                .testID(modelDTO.getTestID())
                .title(modelDTO.getTitle())
                .description(modelDTO.getDescription())
                .category(modelDTO.getCategory())
                .creatingDate(modelDTO.getCreatingDate())
                .creatorID(modelDTO.getCreatorID())
                .isArchived(modelDTO.isArchived())
                .updatingDate(modelDTO.getUpdatingDate())
                .questions(modelDTO.getQuestions())
                .tags(modelDTO.getTags())
                .build();
    }

    public static TestDTO fromEntityToDTO(TestEntity entity) {
        return TestDTO.builder()
                .testID(entity.getTestID())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .creatingDate(entity.getCreatingDate())
                .creatorID(entity.getCreatorID())
                .isArchived(entity.isArchived())
                .updatingDate(entity.getUpdatingDate())
                .questions(entity.getQuestions())
                .tags(entity.getTags())
                .build();
    }

    public static TestEntity createTestClone(TestEntity test) {
        return TestEntity.builder()
                .testID(test.getTestID())
                .title(test.getTitle())
                .description(test.getDescription())
                .category(test.getCategory())
                .creatingDate(test.getCreatingDate())
                .creatorID(test.getCreatorID())
                .isArchived(test.isArchived())
                .updatingDate(test.getUpdatingDate())
                .questions(test.getQuestions())
                .tags(test.getTags())
                .build();
    }

    public static TestResultDTO fromEntityToDTO(TestResultEntity entity) {
        return TestResultDTO.builder()
                .resultID(entity.getResultID())
                .testID(entity.getTestID())
                .userID(entity.getUserID())
                .correctAnswersCount(entity.getCorrectAnswersCount())
                .correctAnswersPercentage(entity.getCorrectAnswersPercentage())
                .testingDate(entity.getTestingDate())
                .build();
    }

    public static TestResultEntity fromDTOToEntity(TestResultDTO entityDTO) {
        return TestResultEntity.builder()
                .resultID(entityDTO.getResultID())
                .testID(entityDTO.getTestID())
                .userID(entityDTO.getUserID())
                .correctAnswersCount(entityDTO.getCorrectAnswersCount())
                .correctAnswersPercentage(entityDTO.getCorrectAnswersPercentage())
                .testingDate(entityDTO.getTestingDate())
                .build();
    }
}
