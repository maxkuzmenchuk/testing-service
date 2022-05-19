package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.QuestionEntity;

public interface IOptionsService {
    void updateOptions(QuestionEntity editedQuestion, Long updateUserID);
}
