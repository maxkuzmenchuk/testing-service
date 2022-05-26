package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.QuestionNotFoundException;
import com.kzumenchuk.testingservice.repository.IQuestionRepository;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.service.interfaces.IOptionsService;
import com.kzumenchuk.testingservice.service.interfaces.IQuestionService;
import com.kzumenchuk.testingservice.service.interfaces.IUpdateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
    private final IQuestionRepository IQuestionRepository;
    private final IOptionsService optionsService;
    private final IUpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editQuestions(Set<QuestionEntity> editedQuestions, Long updateUserID) {
        editedQuestions.stream()
                .filter(Objects::nonNull)
                .forEach((question) -> {
                    Optional<QuestionEntity> databaseQuestionData = IQuestionRepository.findById(question.getQuestionID());

                    if (databaseQuestionData.isPresent()) {
                        QuestionEntity questionEntity = databaseQuestionData.get();

                        optionsService.updateOptions(question, updateUserID);

                        if (!question.equals(questionEntity)) {
                            updateLogService.logQuestionUpdate(questionEntity, question, updateUserID);

                            questionEntity.setTitle(question.getTitle());
                            questionEntity.setDescription(question.getDescription());
                            questionEntity.setUpdatingDate(LocalDateTime.now());

                            IQuestionRepository.save(questionEntity);
                        }
                    } else {
                        throw new QuestionNotFoundException("Question (id = " + question.getQuestionID() + ") not found");
                    }
                });
    }
}
