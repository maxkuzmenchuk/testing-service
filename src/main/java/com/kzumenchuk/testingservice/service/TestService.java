package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.TestAlreadyExistsException;
import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.TestRepository;
import com.kzumenchuk.testingservice.repository.model.*;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.util.EntityMapper;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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
    public TestDTO addNewTest(TestDTO testDTO) {
        boolean isExists = false;
        List<TestEntity> testEntityList = testRepository.getTestEntitiesByTitle(testDTO.getTitle());


        for (TestEntity testData : testEntityList) {
            if (testData.equals(EntityMapper.fromDTOToEntity(testDTO)) || testData.getQuestions().equals(testDTO.getQuestions())) {
                isExists = true;
                break;
            }
        }

        if (!isExists) {
            TestEntity testEntity = TestEntity.builder()
                    .title(testDTO.getTitle())
                    .description(testDTO.getDescription())
                    .category(testDTO.getCategory())
                    .createDate(LocalDate.now())
                    .createUserID(testDTO.getCreateUserID())
                    .updateDate(LocalDateTime.now())
                    .build();

            Set<QuestionEntity> questions = testDTO.getQuestions();
            Set<TagEntity> tags = testDTO.getTags();

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

            return EntityMapper.fromEntityToDTO(savedTest);
        } else {
            throw new TestAlreadyExistsException("Test is already exists");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public Map<String, Object> editTest(TestDTO editedTestData) {
        Optional<TestEntity> databaseTestData = testRepository.findById(editedTestData.getTestID());
        Map<String, Object> hmResult = new HashMap<>();

        if (databaseTestData.isPresent()) {
            TestEntity test = databaseTestData.get();

            questionService.editQuestions(editedTestData.getQuestions(), 1L);
            tagsService.editTags(editedTestData.getTags(), 1L);

            if (!test.equals(EntityMapper.fromDTOToEntity(editedTestData))) {
                logUpdate(test, editedTestData, 1L);

                test.setTitle(editedTestData.getTitle());
                test.setDescription(editedTestData.getDescription());
                test.setCategory(editedTestData.getCategory());
                test.setUpdateDate(LocalDateTime.now());

                TestEntity updatedTest = testRepository.saveAndFlush(test);
                hmResult.put("message", "Test updated successfully");
                hmResult.put("result", EntityMapper.fromEntityToDTO(updatedTest));

                return hmResult;
            } else {
                hmResult.put("message", "No updates needed");
                hmResult.put("result", editedTestData);

                return hmResult;
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

    public TestDTO getTestByID(Long id) {
        Optional<TestEntity> testEntityOptional = testRepository.findById(id);

        if (testEntityOptional.isPresent()) {
            TestEntity testEntity = testEntityOptional.get();

            return EntityMapper.fromEntityToDTO(testEntity);
        } else {
            throw new TestNotFoundException("Test not found");
        }
    }

    public List<TestDTO> getAllTests() {
        List<TestEntity> testEntityList = testRepository.findAll();

        return testEntityList.stream()
                .map(EntityMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<TestDTO> getTestsByTitle(String title) {
        List<TestEntity> testEntityList = testRepository.getTestEntitiesByTitle(title);

        return testEntityList.stream()
                .map(EntityMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<TestDTO> getTestsByCategory(String category) {
        List<TestEntity> testEntityList = testRepository.getTestEntitiesByCategory(category);

        return testEntityList.stream()
                .map(EntityMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<TestDTO> getTestsByCreateDate(LocalDate date) {
        List<TestEntity> testEntityList = testRepository.getTestEntitiesByCreateDate(date);

        return testEntityList.stream()
                .map(EntityMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public void logUpdate(TestEntity oldTest, TestDTO newTest, Long updateUserID) {
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
