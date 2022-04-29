package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.QuestionRepository;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final OptionsService optionsService;

    public QuestionService(QuestionRepository questionRepository, OptionsService optionsService) {
        this.questionRepository = questionRepository;
        this.optionsService = optionsService;
    }

    public void editQuestions(Set<QuestionEntity> editedQuestions) {
        editedQuestions.stream()
                .filter(Objects::nonNull)
                .forEach((question) -> {
                    Optional<QuestionEntity> databaseQuestionData = questionRepository.findById(question.getQuestionID());

                    if (databaseQuestionData.isPresent()) {
                        QuestionEntity questionEntity = databaseQuestionData.get();

                        optionsService.updateOptions(question);

                        questionEntity.setTitle(question.getTitle());
                        questionEntity.setDescription(question.getDescription());
                        questionEntity.setUpdateDate(LocalDateTime.now());

                        questionRepository.save(questionEntity);
                    }
                });
    }
}
