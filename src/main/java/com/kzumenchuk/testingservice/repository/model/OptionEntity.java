package com.kzumenchuk.testingservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "test_options")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long testID;

    @Column(name = "option_value")
    private String value;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private TestEntity test;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OptionEntity that = (OptionEntity) o;
        return testID != null && Objects.equals(testID, that.testID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "OptionEntity{" +
                "testID=" + testID +
                ", value='" + value + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
