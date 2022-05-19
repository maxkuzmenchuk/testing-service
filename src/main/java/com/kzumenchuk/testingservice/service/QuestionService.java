package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.QuestionNotFoundException;
import com.kzumenchuk.testingservice.repository.QuestionRepository;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.service.interfaces.IQuestionService;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
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
    private final QuestionRepository questionRepository;
    private final OptionsService optionsService;
    private final UpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editQuestions(Set<QuestionEntity> editedQuestions, Long updateUserID) {
        editedQuestions.stream()
                .filter(Objects::nonNull)
                .forEach((question) -> {
                    Optional<QuestionEntity> databaseQuestionData = questionRepository.findById(question.getQuestionID());

                    if (databaseQuestionData.isPresent()) {
                        QuestionEntity questionEntity = databaseQuestionData.get();

                        optionsService.updateOptions(question, updateUserID);

                        if (!question.equals(questionEntity)) {
                            logQuestionUpdate(questionEntity, question, updateUserID);

                            questionEntity.setTitle(question.getTitle());
                            questionEntity.setDescription(question.getDescription());
                            questionEntity.setUpdateDate(LocalDateTime.now());

                            questionRepository.save(questionEntity);
                        }
                    } else {
                        throw new QuestionNotFoundException("Question (id = " + question.getQuestionID() + ") not found");
                    }
                });
    }

    @Transactional(rollbackFor = Exception.class)
    public void logQuestionUpdate(QuestionEntity oldQuestion, QuestionEntity newQuestion, Long updateUserID) {
        if (!oldQuestion.getTitle().equalsIgnoreCase(newQuestion.getTitle())) {
            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(oldQuestion.getQuestionID(), EntityType.QUESTION,
                    "U", "title", oldQuestion.getTitle(), newQuestion.getTitle(), updateUserID);
            updateLogService.saveLog(tagLog);
        }

        if (!oldQuestion.getDescription().equalsIgnoreCase(newQuestion.getDescription())) {
            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(oldQuestion.getQuestionID(), EntityType.QUESTION,
                    "U", "description", oldQuestion.getDescription(), newQuestion.getDescription(), updateUserID);
            updateLogService.saveLog(tagLog);
        }
    }
}
