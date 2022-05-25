package com.kzumenchuk.testingservice.service.interfaces;


import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import com.kzumenchuk.testingservice.util.enums.FileKind;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface IFileDataService {
    FileDataEntity generateTestDataToPDF(Long testID);

    FileDataEntity generateTestResultToPDF(Long resultID);

    FileDataEntity storeFileData(File file, Long testID, FileKind fileKind);

    FileDataEntity getTestFile(Long testID, FileKind fileKind) throws FileNotFoundException;

    FileDataEntity getTestResultFile(Long resultID) throws FileNotFoundException;

    Resource returnFileAsResource(Path sourcePath) throws FileNotFoundException;

    void updateTestFileData(Long testID);

    void deleteTestFileData(Long testID, FileKind fileKind);

    void deleteTestResultFileData(Long resultID);
}
