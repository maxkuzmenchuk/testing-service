package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFileDataRepository extends JpaRepository<FileDataEntity, Long> {
    Optional<FileDataEntity> getFileDataEntityByTestID(Long id);

    void deleteAllByTestID(Long testID);


}
