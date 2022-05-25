package com.kzumenchuk.testingservice.repository.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "test_archive")
public class ArchiveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Long archiveID;

    @Column(name = "archiving_date")
    private LocalDate archivingDate;

    @Column(name = "archiving_user_id")
    private Long archivingUserID;

    @Column(name = "archiving_test_id")
    private Long testID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArchiveEntity archive = (ArchiveEntity) o;
        return archiveID != null && Objects.equals(archiveID, archive.archiveID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
