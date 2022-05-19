package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.QuestionEntity;

import java.util.Set;

public interface IQuestionService {
    void editQuestions(Set<QuestionEntity> editedQuestions, Long updateUserID);
}
