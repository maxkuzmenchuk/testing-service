package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionRepository extends JpaRepository<QuestionEntity, Long> {
}
