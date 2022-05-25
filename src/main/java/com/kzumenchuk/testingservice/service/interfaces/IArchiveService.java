package com.kzumenchuk.testingservice.service.interfaces;


import com.kzumenchuk.testingservice.repository.model.ArchiveEntity;
import com.kzumenchuk.testingservice.util.enums.OperationType;

import java.util.List;

public interface IArchiveService {
    void moveToArchive(Long testID);

    void restoreFromArchive(Long testID);

    void deleteFromArchive(Long archiveID, OperationType reason);

    ArchiveEntity getArchiveById(Long id);

    List<ArchiveEntity> getAllArchive();
}
