package com.kzumenchuk.testingservice.util;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;

public class EntityMapper {
    public static TestEntity fromDTOToEntity(TestDTO modelDTO) {
        return TestEntity.builder()
                .testID(modelDTO.getTestID())
                .title(modelDTO.getTitle())
                .description(modelDTO.getDescription())
                .category(modelDTO.getCategory())
                .createDate(modelDTO.getCreateDate())
                .createUserID(modelDTO.getCreateUserID())
                .updateDate(modelDTO.getUpdateDate())
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
                .createDate(entity.getCreateDate())
                .createUserID(entity.getCreateUserID())
                .updateDate(entity.getUpdateDate())
                .questions(entity.getQuestions())
                .tags(entity.getTags())
                .build();
    }
}
