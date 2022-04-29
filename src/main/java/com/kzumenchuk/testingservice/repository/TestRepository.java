package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
    List<TestEntity> getTestEntitiesByCategory(String category);
    List<TestEntity> getTestEntitiesByTitle(String title);
    List<TestEntity> getTestEntitiesByCreateDate(LocalDate date);

}
