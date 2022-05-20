package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.*;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;

public interface IUpdateLogService {
    void logTestInsert(Long testID);

    void logTestUpdate(TestEntity oldTest, TestDTO newTest, Long updateUserID);

    void logTestDelete(Long testID);

    void logTestArchiving(Long testID);

    void logArchiveRestoring(Long archiveID, Long userID);

    void logArchiveDeleting(Long archiveID, Long userID);

    void logQuestionUpdate(QuestionEntity oldQuestion, QuestionEntity newQuestion, Long updateUserID);

    void logOptionUpdate(OptionEntity oldOption, OptionEntity newOption, Long updateUserID);

    void logTagsUpdate(Long updateUserID, TagEntity editedTag, TagEntity tag);

    void saveLog(UpdateLogEntity logEntity);
}
