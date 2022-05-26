package com.kzumenchuk.testingservice.util;

import com.kzumenchuk.testingservice.repository.ITestRepository;
import com.kzumenchuk.testingservice.repository.ITestResultRepository;
import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import com.kzumenchuk.testingservice.util.enums.FileKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Getter
@RequiredArgsConstructor
public class FileDataUtil {
    private final String TEST_FILE_SOURCE_URL = "files/tests/";
    private final String TEST_RESULT_FILE_SOURCE_URL = "files/results/";

    private final ITestRepository testRepository;

    private final ITestResultRepository testResultRepository;

    public Path returnSourceUrl(FileKind fileKind) {
        try {
            Path filesDirectory = Paths.get("files/");
            Path testFileSourceDirectory = Paths.get(TEST_FILE_SOURCE_URL);
            Path resultsSourceDirectory = Paths.get(TEST_RESULT_FILE_SOURCE_URL);

            if (!Files.isDirectory(filesDirectory)) {
                Files.createDirectory(filesDirectory);
            }

            // create directories if they are not exists
            switch (fileKind) {
                case TEST:
                    if (!Files.isDirectory(testFileSourceDirectory)) {
                        Files.createDirectory(testFileSourceDirectory);
                    }
                    return testFileSourceDirectory;
                case RESULT:
                    if (!Files.isDirectory(resultsSourceDirectory)) {
                        Files.createDirectory(resultsSourceDirectory);
                    }

                    return resultsSourceDirectory;
                default:
                    return filesDirectory;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TestEntity getTest(Long testID) {
        return testRepository.getById(testID);
    }

    public TestResultEntity getTestResult(Long resultID) {
        return testResultRepository.getById(resultID);
    }
}
