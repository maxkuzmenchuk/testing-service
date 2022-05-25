package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestResultDTO;
import com.kzumenchuk.testingservice.util.requests.DeleteRequest;

import java.util.List;

public interface ITestResultService {
    TestResultEntity saveTestResults(TestResultDTO testResultDTO);

    List<TestResultDTO> getResultsByTestID(Long testID);

    void deleteTestResult(DeleteRequest deleteRequest);
}
