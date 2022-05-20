package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.IUpdateLogRepository;
import com.kzumenchuk.testingservice.repository.model.*;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.service.interfaces.IUpdateLogService;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.OperationType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateLogService implements IUpdateLogService {
    private final IUpdateLogRepository IUpdateLogRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logTestInsert(Long testID) {
        UpdateLogEntity insertTestLog = UpdateLogUtil.createLogEntity(testID, EntityType.TEST,
                OperationType.INSERT, "", "", "", 1L);
        saveLog(insertTestLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void logTestUpdate(TestEntity oldTest, TestDTO newTest, Long updateUserID) {
        UpdateLogEntity logEntity;

        if (!oldTest.getTitle().equalsIgnoreCase(newTest.getTitle())) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    OperationType.UPDATE, "title", oldTest.getTitle(), newTest.getTitle(), updateUserID);
            saveLog(logEntity);
        }

        if (!oldTest.getDescription().equalsIgnoreCase(newTest.getDescription())) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    OperationType.UPDATE, "description", oldTest.getDescription(), newTest.getDescription(), updateUserID);
            saveLog(logEntity);
        }

        if (!oldTest.getCategory().equalsIgnoreCase(newTest.getCategory())) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    OperationType.UPDATE, "category", oldTest.getCategory(), newTest.getCategory(), updateUserID);
            saveLog(logEntity);
        }

        if (oldTest.isArchived() != newTest.isArchived()) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    OperationType.UPDATE, "isArchived", String.valueOf(oldTest.isArchived()), String.valueOf(newTest.isArchived()), updateUserID);
            saveLog(logEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logTestDelete(Long testID) {
        UpdateLogEntity deletedTestLog = UpdateLogUtil.createLogEntity(testID, EntityType.TEST,
                OperationType.DELETE, "", "", "", 1L);
        saveLog(deletedTestLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logTestArchiving(Long testID) {
        UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(testID, EntityType.TEST,
                OperationType.ARCHIVE, "", "", "", 1L);
        saveLog(logEntity);
    }


    @Override
    public void logArchiveRestoring(Long archiveID, Long userID) {
        UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(archiveID, EntityType.ARCHIVE, OperationType.RESTORE,
                "", "", "", userID);

        saveLog(logEntity);
    }

    @Override
    public void logArchiveDeleting(Long archiveID, Long userID) {
        UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(archiveID, EntityType.ARCHIVE, OperationType.DELETE,
                "archiveID", String.valueOf(archiveID), "", userID);

        saveLog(logEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logQuestionUpdate(QuestionEntity oldQuestion, QuestionEntity newQuestion, Long updateUserID) {
        if (!oldQuestion.getTitle().equalsIgnoreCase(newQuestion.getTitle())) {
            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(oldQuestion.getQuestionID(), EntityType.QUESTION,
                    OperationType.UPDATE, "title", oldQuestion.getTitle(), newQuestion.getTitle(), updateUserID);
            saveLog(tagLog);
        }

        if (!oldQuestion.getDescription().equalsIgnoreCase(newQuestion.getDescription())) {
            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(oldQuestion.getQuestionID(), EntityType.QUESTION,
                    OperationType.UPDATE, "description", oldQuestion.getDescription(), newQuestion.getDescription(), updateUserID);
            saveLog(tagLog);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logOptionUpdate(OptionEntity oldOption, OptionEntity newOption, Long updateUserID) {
        if (!oldOption.getValue().equalsIgnoreCase(newOption.getValue())) {
            UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(oldOption.getOptionID(), EntityType.OPTION,
                    OperationType.UPDATE, "value", oldOption.getValue(), newOption.getValue(), updateUserID);
            saveLog(logEntity);
        }

        if (oldOption.isCorrect() != newOption.isCorrect()) {
            UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(oldOption.getOptionID(), EntityType.OPTION,
                    OperationType.UPDATE, "isCorrect", String.valueOf(oldOption.isCorrect()), String.valueOf(newOption.isCorrect()), updateUserID);
            saveLog(logEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logTagsUpdate(Long updateUserID, TagEntity editedTag, TagEntity tag) {
        UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(tag.getTagID(), EntityType.TAG,
                OperationType.UPDATE, "value", tag.getValue(), editedTag.getValue(), updateUserID);
        saveLog(logEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLog(UpdateLogEntity logEntity) {
        IUpdateLogRepository.saveAndFlush(logEntity);
    }
}
