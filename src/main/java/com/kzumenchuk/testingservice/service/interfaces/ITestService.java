package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;

import java.util.List;
import java.util.Map;

public interface ITestService {
    TestDTO addNewTest(TestDTO testDTO);

    Map<String, Object> editTest(TestDTO editedTestData);

    void deleteTestById(Long[] IDs);

    TestDTO getTestByID(Long id);

    List<TestDTO> getTests(String searchType, String searchValue);
}
