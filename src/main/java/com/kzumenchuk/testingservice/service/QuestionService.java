package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.QuestionRepository;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final OptionsService optionsService;
    private final UpdateLogService updateLogService;

    public QuestionService(QuestionRepository questionRepository, OptionsService optionsService, UpdateLogService updateLogService) {
        this.questionRepository = questionRepository;
        this.optionsService = optionsService;
        this.updateLogService = updateLogService;
    }

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
                    }
                });
    }

    private void logQuestionUpdate(QuestionEntity oldQuestion, QuestionEntity newQuestion, Long updateUserID) {
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
