package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.ITestResultRepository;
import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.repository.model.dto.TestResultDTO;
import com.kzumenchuk.testingservice.service.interfaces.ITestResultService;
import com.kzumenchuk.testingservice.service.interfaces.ITestService;
import com.kzumenchuk.testingservice.service.interfaces.IUpdateLogService;
import com.kzumenchuk.testingservice.util.EntityMapper;
import com.kzumenchuk.testingservice.util.requests.DeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TestResultService implements ITestResultService {
    private final ITestResultRepository testResultRepository;

    private final ITestService testService;

    private final IUpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestResultEntity saveTestResults(TestResultDTO testResultDTO) {
        TestDTO testDTO = testService.getTestByID(testResultDTO.getTestID());
        int questionsCount = testDTO.getQuestions().size();
        int correctAnswersCount = testResultDTO.getCorrectAnswersCount();
        int correctAnswerPercentage = countPercentage(questionsCount, correctAnswersCount);

        TestResultEntity testResult = TestResultEntity.builder()
                .testID(testResultDTO.getTestID())
                .userID(testResultDTO.getUserID())
                .correctAnswersCount(correctAnswersCount)
                .correctAnswersPercentage(correctAnswerPercentage)
                .testingDate(LocalDateTime.now())
                .build();

        return testResultRepository.saveAndFlush(testResult);
    }

    @Override
    public List<TestResultDTO> getResultsByTestID(Long testID) {
        List<TestResultEntity> testResultList = testResultRepository.getTestResultEntitiesByTestID(testID);

        return testResultList.stream()
                .map(EntityMapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    private int countPercentage(double questionsCount, double correctAnswersCount) {
        double percentage = (correctAnswersCount * 100) / questionsCount;

        return (int) Math.round(percentage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTestResult(DeleteRequest deleteRequest) {
        Long userID = deleteRequest.getUserID();
        Long[] IDs = deleteRequest.getDeleteIDs();
        Stream.of(IDs)
                .filter(Objects::nonNull)
                .forEach((resultID) -> {
                    testResultRepository.deleteById(resultID);
//                    fileDataService.deleteFileData(resultID);

                    updateLogService.logResultDelete(resultID, userID);
                });
    }
}
