package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.util.requests.DeleteRequest;

import java.util.List;
import java.util.Map;

public interface ITestService {
    TestDTO addNewTest(TestDTO testDTO);

    Map<String, Object> editTest(TestDTO editedTestData);

    void deleteTestById(DeleteRequest deleteRequest);

    void deleteTestById(Long id, Long userID);

    TestDTO getTestByID(Long id);

    List<TestDTO> getTests(String searchType, String searchValue);
}
