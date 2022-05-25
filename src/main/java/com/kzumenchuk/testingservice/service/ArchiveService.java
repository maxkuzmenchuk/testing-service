package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.ArchiveNotFoundException;
import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.IArchiveRepository;
import com.kzumenchuk.testingservice.repository.ITestRepository;
import com.kzumenchuk.testingservice.repository.model.ArchiveEntity;
import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.service.interfaces.IArchiveService;
import com.kzumenchuk.testingservice.service.interfaces.IUpdateLogService;
import com.kzumenchuk.testingservice.util.EntityMapper;
import com.kzumenchuk.testingservice.util.enums.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArchiveService implements IArchiveService {
    private final IArchiveRepository archiveRepository;
    private final ITestRepository testRepository;

    private final IUpdateLogService updateLogService;

    private final TestService testService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToArchive(Long testID) {
        Optional<TestEntity> optionalTest = testRepository.findById(testID);

        if (optionalTest.isPresent()) {
            TestEntity test = optionalTest.get();
            TestEntity tempTest = EntityMapper.createTestClone(test); // test entity with old values. Created for logging

            ArchiveEntity archive = ArchiveEntity.builder()
                    .archivingDate(LocalDate.now())
                    .archivingUserID(1L)
                    .testID(testID)
                    .build();
            archiveRepository.saveAndFlush(archive);


            test.setArchived(true);
            test.setUpdatingDate(LocalDateTime.now());
            TestEntity savedTest = testRepository.saveAndFlush(test);

            updateLogService.logTestArchiving(savedTest.getTestID());
            updateLogService.logTestUpdate(tempTest, EntityMapper.fromEntityToDTO(savedTest), 1L);

        } else {
            throw new TestNotFoundException("Test not found");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void restoreFromArchive(Long testID) {
        Optional<TestEntity> optionalTest = testRepository.findById(testID);

        if (optionalTest.isPresent()) {
            TestEntity testEntity = optionalTest.get();
            TestEntity tempTest = EntityMapper.createTestClone(testEntity); // test entity with old values. Created for logging

            Optional<ArchiveEntity> optionalArchive = archiveRepository.findByTestID(testID);

            if (optionalArchive.isPresent()) {
                ArchiveEntity archive = optionalArchive.get();
                deleteFromArchive(archive.getTestID(), OperationType.RESTORE);

                testEntity.setArchived(false);
                testEntity.setUpdatingDate(LocalDateTime.now());
                TestEntity savedTest = testRepository.saveAndFlush(testEntity);

                updateLogService.logTestUpdate(tempTest, EntityMapper.fromEntityToDTO(savedTest), 1L);
            } else {
                throw new ArchiveNotFoundException("Archive data not found");
            }
        } else {
            throw new TestNotFoundException("Test not found");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFromArchive(Long testID, OperationType reason) {
        archiveRepository.deleteByTestID(testID);

        switch (reason) {
            case RESTORE:
                updateLogService.logArchiveRestoring(testID, 1L);
                break;
            case DELETE:
                Optional<TestEntity> optionalTest = testRepository.findById(testID);

                if (optionalTest.isPresent()) {
                    testService.deleteTestById(new Long[]{testID});
                    updateLogService.logTestDelete(testID);
                }
                updateLogService.logArchiveDeleting(testID, 1L);
                break;
        }
    }

    @Override
    public ArchiveEntity getArchiveById(Long id) {
        Optional<ArchiveEntity> optionalArchive = archiveRepository.findByTestID(id);

        if (optionalArchive.isPresent()) {
            return optionalArchive.get();
        } else {
            throw new ArchiveNotFoundException("Archive not found");
        }
    }

    @Override
    public List<ArchiveEntity> getAllArchive() {
        return archiveRepository.findAll();
    }
}
