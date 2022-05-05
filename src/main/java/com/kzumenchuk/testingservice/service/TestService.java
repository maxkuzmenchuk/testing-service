package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.TestAlreadyExistsException;
import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.TestRepository;
import com.kzumenchuk.testingservice.repository.model.*;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final QuestionService questionService;
    private final TagsService tagsService;

    private final UpdateLogService updateLogService;

    public TestService(TestRepository testRepository, QuestionService questionService, TagsService tagsService, UpdateLogService updateLogService) {
        this.testRepository = testRepository;
        this.questionService = questionService;
        this.tagsService = tagsService;
        this.updateLogService = updateLogService;
    }

    @Transactional(rollbackOn = Exception.class)
    public TestEntity addNewTest(TestEntity test) {
        boolean isExists = false;
        List<TestEntity> testEntityList = testRepository.getTestEntitiesByTitle(test.getTitle());

        for (TestEntity testData : testEntityList) {
            if (testData.equals(test) || testData.getQuestions().equals(test.getQuestions())) {
                isExists = true;
                break;
            }
        }

        if (!isExists) {
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

            TestEntity savedTest = testRepository.saveAndFlush(testEntity);

            UpdateLogEntity insertTestLog = UpdateLogUtil.createLogEntity(savedTest.getTestID(), EntityType.TEST,
                    "I", "", "", "", 1L);
            updateLogService.saveLog(insertTestLog);

            return savedTest;
        } else {
            throw new TestAlreadyExistsException("Test is already exists");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public TestEntity editTest(TestEntity editedTestData) {
        Optional<TestEntity> databaseTestData = testRepository.findById(editedTestData.getTestID());

        if (databaseTestData.isPresent()) {
            TestEntity test = databaseTestData.get();

            questionService.editQuestions(editedTestData.getQuestions(), 1L);
            tagsService.editTags(editedTestData.getTags(), 1L);

            if (!test.equals(editedTestData)) {
                logUpdate(test, editedTestData, 1L);

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

    @Transactional(rollbackOn = Exception.class)
    public void deleteTestById(Long[] IDs) {
        Stream.of(IDs)
                .filter(Objects::nonNull)
                .forEach((id) -> {
                    testRepository.deleteById(id);

                    UpdateLogEntity deletedTestLog = UpdateLogUtil.createLogEntity(id, EntityType.TEST,
                            "D", "", "", "", 1L);
                    updateLogService.saveLog(deletedTestLog);
                });
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

    public void logUpdate(TestEntity oldTest, TestEntity newTest, Long updateUserID) {
        UpdateLogEntity testLog;

        if (!oldTest.getTitle().equalsIgnoreCase(newTest.getTitle())) {
            testLog = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    "U", "title", oldTest.getTitle(), newTest.getTitle(), updateUserID);
            updateLogService.saveLog(testLog);
        }

        if (!oldTest.getDescription().equalsIgnoreCase(newTest.getDescription())) {
            testLog = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    "U", "description", oldTest.getDescription(), newTest.getDescription(), updateUserID);
            updateLogService.saveLog(testLog);
        }

        if (!oldTest.getCategory().equalsIgnoreCase(newTest.getCategory())) {
            testLog = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    "U", "category", oldTest.getCategory(), newTest.getCategory(), updateUserID);
            updateLogService.saveLog(testLog);
        }
    }
}
