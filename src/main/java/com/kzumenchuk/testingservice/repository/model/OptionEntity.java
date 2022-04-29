package com.kzumenchuk.testingservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@DynamicUpdate
@Table(name = "question_options")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionID;

    @Column(name = "option_value")
    private String value;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private QuestionEntity question;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OptionEntity that = (OptionEntity) o;
        return optionID != null && Objects.equals(optionID, that.optionID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
