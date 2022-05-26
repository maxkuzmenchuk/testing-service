package com.kzumenchuk.testingservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "test_tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagID;

    @Column(name = "tag_value")
    private String value;

    @Column(name = "updating_date")
    private LocalDateTime updatingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private TestEntity testEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TagEntity tagEntity = (TagEntity) o;
        return Objects.equals(value, tagEntity.value);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
