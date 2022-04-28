package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TestResultRepository;
import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import org.springframework.stereotype.Service;

@Service
public class TestResultService {
    private final TestResultRepository testResultRepository;

    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    public TestResultEntity saveTestResults(TestResultEntity testResult) {
        return testResultRepository.saveAndFlush(testResult);
    }
}
