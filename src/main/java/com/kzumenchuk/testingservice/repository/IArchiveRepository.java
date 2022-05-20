package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.ArchiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IArchiveRepository extends JpaRepository<ArchiveEntity, Long> {

    Optional<ArchiveEntity> findByTestID(Long testID);

    void deleteByTestID(Long id);
}
