package com.kzumenchuk.testingservice.service.interfaces;


import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface IFileDataService {
        FileDataEntity generateTestDataToPDF(Long id);

        FileDataEntity storeFileData(File file, Long testID);

        FileDataEntity getFileByTestID(Long testID) throws FileNotFoundException;

        Resource returnFileAsResource(Path sourcePath) throws FileNotFoundException;

        void updateFileData(Long testID);

        void deleteFileData(Long testID);
}
