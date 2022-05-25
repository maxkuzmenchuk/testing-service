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
@Table(name = "files")
public class FileDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    public Long fileID;

    @Column(name = "test_id")
    private Long testID;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_source_url")
    private String fileSourceUrl;

    @Column(name = "creating_date")
    private LocalDateTime creatingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FileDataEntity that = (FileDataEntity) o;
        return fileID != null && Objects.equals(fileID, that.fileID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
