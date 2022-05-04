package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.TestRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import com.kzumenchuk.testingservice.repository.model.TestEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final QuestionService questionService;
    private final TagsService tagsService;

    public TestService(TestRepository testRepository, QuestionService questionService, TagsService tagsService) {
        this.testRepository = testRepository;
        this.questionService = questionService;
        this.tagsService = tagsService;
    }

    // TODO: ADD COMPARING TESTS BEFORE ADDING
    public TestEntity addNewTest(TestEntity test) {
        TestEntity testEntity = TestEntity.builder()
                .title(test.getTitle())
                .description(test.getDescription())
                .category(test.getCategory())
                .createDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .build();

        Set<QuestionEntity> questions = test.getQuestions();
        Set<TagEntity> tags = test.getTags();

        for (QuestionEntity q : questions) {
            Set<OptionEntity> options = q.getOptions();
            options.stream()
                    .filter(Objects::nonNull)
                    .forEach((x) -> {
                        x.setUpdateDate(LocalDateTime.now());
                        x.setQuestion(q);
                    });
        }

        questions.stream()
                .filter(Objects::nonNull)
                .forEach((x) -> {
                    x.setUpdateDate(LocalDateTime.now());
                    x.setTest(testEntity);
                });

        tags.stream()
                .filter(Objects::nonNull)
                .forEach(x -> {
                    x.setUpdateDate(LocalDateTime.now());
                    x.setTestEntity(testEntity);
                });

        testEntity.setQuestions(questions);
        testEntity.setTags(tags);

        return testRepository.save(testEntity);
    }

    // TODO: ADD UPDATE HISTORY
    @Transactional(rollbackOn = Exception.class)
    public TestEntity editTest(TestEntity editedTestData) {
        Optional<TestEntity> databaseTestData = testRepository.findById(editedTestData.getTestID());

        if (databaseTestData.isPresent()) {
            TestEntity test = databaseTestData.get();

            questionService.editQuestions(editedTestData.getQuestions());
            tagsService.editTags(editedTestData.getTags());

            if (!test.equals(editedTestData)) {
                test.setTitle(editedTestData.getTitle());
                test.setDescription(editedTestData.getDescription());
                test.setCategory(editedTestData.getCategory());
                test.setUpdateDate(LocalDateTime.now());

                return testRepository.saveAndFlush(test);
            } else {
                return testRepository.getById(editedTestData.getTestID());
            }
        } else {
            throw new TestNotFoundException("Test not found");
        }
    }

    public TestEntity getTestByID(Long id) {
        return testRepository.findById(id).orElse(new TestEntity());
    }

    public List<TestEntity> getAllTests() {
        return testRepository.findAll();
    }

    public List<TestEntity> getTestsByTitle(String title) {
        return testRepository.getTestEntitiesByTitle(title);
    }

    public List<TestEntity> getTestsByCategory(String category) {
        return testRepository.getTestEntitiesByCategory(category);
    }

    public List<TestEntity> getTestsByCreateDate(LocalDate date) {
        return testRepository.getTestEntitiesByCreateDate(date);
    }
}
