package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TestResultRepository;
import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import com.kzumenchuk.testingservice.service.interfaces.ITestResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestResultService implements ITestResultService {
    private final TestResultRepository testResultRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestResultEntity saveTestResults(TestResultEntity testResult) {
        return testResultRepository.saveAndFlush(testResult);
    }
}
