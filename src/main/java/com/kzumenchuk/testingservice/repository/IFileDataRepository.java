package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import com.kzumenchuk.testingservice.util.enums.FileKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFileDataRepository extends JpaRepository<FileDataEntity, Long> {
    @Query("select file from FileDataEntity file where file.testID = :testID and file.fileKind = :fileKind")
    Optional<FileDataEntity> getTestFile(@Param("testID") Long testID, @Param("fileKind") FileKind fileKind);

    @Modifying
    @Query("delete from FileDataEntity where testID = :testID and fileKind = :fileKind")
    void deleteTestFile(@Param("testID") Long testID, @Param("fileKind") FileKind fileKind);


}
