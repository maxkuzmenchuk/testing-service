package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TestRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import com.kzumenchuk.testingservice.repository.model.TestEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public TestEntity addNewTest(TestEntity test) {
        TestEntity testEntity = TestEntity.builder()
                .title(test.getTitle())
                .description(test.getDescription())
                .category(test.getCategory())
                .build();

        Set<TagEntity> tags = test.getTags();

        Set<OptionEntity> optionEntities = test.getOptions();

        optionEntities.stream()
                .filter(Objects::nonNull)
                .forEach((x) -> x.setTest(testEntity));
        tags.stream()
                .filter(Objects::nonNull)
                .forEach(x -> x.setTestEntity(testEntity));

        testEntity.setOptions(optionEntities);
        testEntity.setTags(tags);

        return testRepository.save(testEntity);
    }
}
