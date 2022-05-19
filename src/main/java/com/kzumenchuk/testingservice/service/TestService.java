package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.TestAlreadyExistsException;
import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.TestRepository;
import com.kzumenchuk.testingservice.repository.model.*;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.service.interfaces.ITestService;
import com.kzumenchuk.testingservice.util.EntityMapper;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TestService implements ITestService {
    private final TestRepository testRepository;
    private final QuestionService questionService;
    private final TagsService tagsService;
    private final UpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> editTest(TestDTO editedTestData) {
        Optional<TestEntity> databaseTestData = testRepository.findById(editedTestData.getTestID());
        Map<String, Object> hmResult = new HashMap<>();

        if (databaseTestData.isPresent()) {
            TestEntity test = databaseTestData.get();

            questionService.editQuestions(editedTestData.getQuestions(), 1L);
            tagsService.editTags(editedTestData.getTags(), 1L);

            if (!test.equals(EntityMapper.fromDTOToEntity(editedTestData))) {
                logTestUpdate(test, editedTestData, 1L);

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

    @Transactional(rollbackFor = Exception.class)
    @Override
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

    @Override
    public TestDTO getTestByID(Long id) {
        Optional<TestEntity> testEntityOptional = testRepository.findById(id);

        if (testEntityOptional.isPresent()) {
            TestEntity testEntity = testEntityOptional.get();

            return EntityMapper.fromEntityToDTO(testEntity);
        } else {
            throw new TestNotFoundException("Test not found");
        }
    }

    @Override
    public List<TestDTO> getTests(String searchType, String searchValue) {
        List<TestEntity> testEntityList = new ArrayList<>();
        switch (searchType) {
            case "all":
                testEntityList = testRepository.findAll();
                break;
            case "title":
                testEntityList = testRepository.getTestEntitiesByTitle(searchValue);
                break;
            case "category":
                testEntityList = testRepository.getTestEntitiesByCategory(searchValue);
                break;
            case "tag":
                testEntityList = testRepository.getTestEntitiesByTag(searchValue);
                break;
            case "createDate":
                LocalDate date = LocalDate.parse(searchValue);
                testEntityList = testRepository.getTestEntitiesByCreateDate(date);
                break;
        }

        return testEntityList.stream()
                .map(EntityMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void logTestUpdate(TestEntity oldTest, TestDTO newTest, Long updateUserID) {
        UpdateLogEntity logEntity;

        if (!oldTest.getTitle().equalsIgnoreCase(newTest.getTitle())) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    "U", "title", oldTest.getTitle(), newTest.getTitle(), updateUserID);
            updateLogService.saveLog(logEntity);
        }

        if (!oldTest.getDescription().equalsIgnoreCase(newTest.getDescription())) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    "U", "description", oldTest.getDescription(), newTest.getDescription(), updateUserID);
            updateLogService.saveLog(logEntity);
        }

        if (!oldTest.getCategory().equalsIgnoreCase(newTest.getCategory())) {
            logEntity = UpdateLogUtil.createLogEntity(oldTest.getTestID(), EntityType.TEST,
                    "U", "category", oldTest.getCategory(), newTest.getCategory(), updateUserID);
            updateLogService.saveLog(logEntity);
        }
    }
}
