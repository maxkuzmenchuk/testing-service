package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.TestResultEntity;

public interface ITestResultService {
    TestResultEntity saveTestResults(TestResultEntity testResult);
}
