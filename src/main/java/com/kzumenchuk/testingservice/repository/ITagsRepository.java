package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagsRepository extends JpaRepository<TagEntity, Long> {
    // TODO: CREATE REPOSITORY METHODS FOR SEARCHING TESTS BY TAGS
}