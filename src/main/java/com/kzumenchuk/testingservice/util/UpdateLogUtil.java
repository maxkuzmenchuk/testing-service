package com.kzumenchuk.testingservice.util;

import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;

import java.time.LocalDateTime;

public class UpdateLogUtil {
    public static UpdateLogEntity createLogEntity(Long entityID,
                                                  EntityType entityType,
                                                  String operationType,
                                                  String updatedField,
                                                  String oldValue,
                                                  String newValue,
                                                  Long updateUserID) {
        return UpdateLogEntity.builder()
                .entityID(entityID)
                .entityType(entityType)
                .operationType(operationType)
                .updatedField(updatedField)
                .oldValue(oldValue)
                .newValue(newValue)
                .updateDate(LocalDateTime.now())
                .updateUserID(updateUserID)
                .build();
    }
}
