package com.kzumenchuk.testingservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "test_questions")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionID;

    @Column(name = "question_title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private TestEntity test;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "question")
    private Set<OptionEntity> options;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuestionEntity that = (QuestionEntity) o;
        return questionID != null && Objects.equals(questionID, that.questionID)
                && Objects.equals(title, that.title) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
