package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestResultRepository extends JpaRepository<TestResultEntity, Long> {
}
