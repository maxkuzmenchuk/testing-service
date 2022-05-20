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

    @Column(name = "creating_date")
    private LocalDate creatingDate;

    @Column(name = "updating_date")
    private LocalDateTime updatingDate;

    @Column(name = "creator_id")
    private Long creatorID;

    @Column(name = "is_archived")
    private boolean isArchived;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "test")
    private Set<QuestionEntity> questions;

    @OneToMany(mappedBy = "testEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<TagEntity> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestEntity that = (TestEntity) o;
        return Objects.equals(title, that.title)
                && Objects.equals(description, that.description) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
