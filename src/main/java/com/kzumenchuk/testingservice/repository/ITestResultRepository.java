package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITestResultRepository extends JpaRepository<TestResultEntity, Long> {
    // TODO: CREATE METHODS FOR TESTS STATISTIC: NUMBER OF PASSING TESTS, COUNT CORRECT ANSWERS BY TEST, AVERAGE PERCENTAGE OF CORRECT ANSWERS etc..

    List<TestResultEntity> getTestResultEntitiesByTestID(Long testID);

}
