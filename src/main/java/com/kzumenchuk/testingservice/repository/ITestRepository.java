package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITestRepository extends JpaRepository<TestEntity, Long> {
    List<TestEntity> getTestEntitiesByCategory(String category);

    List<TestEntity> getTestEntitiesByTitle(String title);

    List<TestEntity> getTestEntitiesByCreatingDate(LocalDate date);

    @Query("select test from TestEntity test where test.testID in (select tag.testEntity.testID from TagEntity tag where tag.value = :tag)")
    List<TestEntity> getTestEntitiesByTag(@Param("tag") String tag);

}
