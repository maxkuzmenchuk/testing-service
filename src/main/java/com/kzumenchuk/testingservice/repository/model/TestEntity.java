package com.kzumenchuk.testingservice.repository.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tests")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testID;

    @Column(name = "test_title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "test")
    private Set<QuestionEntity> questions;

    @OneToMany(mappedBy = "testEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<TagEntity> tags;


    @Override
    public String toString() {
        return "TestEntity{" +
                "testID=" + testID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", questions=" + questions +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestEntity that = (TestEntity) o;
        return testID != null && Objects.equals(testID, that.testID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
