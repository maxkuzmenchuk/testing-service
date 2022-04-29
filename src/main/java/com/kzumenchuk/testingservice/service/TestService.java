package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TestRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import com.kzumenchuk.testingservice.repository.model.TestEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
                .createDate(LocalDate.now())
                .build();

        Set<QuestionEntity> questions = test.getQuestions();
        Set<TagEntity> tags = test.getTags();

        for (QuestionEntity q : questions) {
            Set<OptionEntity> options = q.getOptions();
            options.stream()
                    .filter(Objects::nonNull)
                    .forEach((x) -> x.setQuestion(q));
        }

        questions.stream()
                .filter(Objects::nonNull)
                .forEach((x) -> x.setTest(testEntity));

        tags.stream()
                .filter(Objects::nonNull)
                .forEach(x -> x.setTestEntity(testEntity));

        testEntity.setQuestions(questions);
        testEntity.setTags(tags);

        return testRepository.save(testEntity);
    }

    public TestEntity getTestByID(Long id) {
        return testRepository.findById(id).orElse(new TestEntity());
    }

    public List<TestEntity> getAllTests() {
        return testRepository.findAll();
    }

    public List<TestEntity> getTestsByTitle(String title) {
        return testRepository.getTestEntitiesByTitle(title);
    }

    public List<TestEntity> getTestsByCategory(String category) {
        return testRepository.getTestEntitiesByCategory(category);
    }

    public List<TestEntity> getTestsByCreateDate(LocalDate date) {
        return testRepository.getTestEntitiesByCreateDate(date);
    }
}
