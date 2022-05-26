package com.kzumenchuk.testingservice.repository.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "test_results")
public class TestResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultID;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "test_id")
    private Long testID;

    @Column(name = "correct_answers_count")
    private int correctAnswersCount;

    @Column(name = "correct_answers_percentage")
    private int correctAnswersPercentage;

    @Column(name = "testing_date")
    private LocalDateTime testingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestResultEntity that = (TestResultEntity) o;
        return resultID != null && Objects.equals(resultID, that.resultID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
