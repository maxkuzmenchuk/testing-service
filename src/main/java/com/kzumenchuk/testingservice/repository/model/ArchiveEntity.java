package com.kzumenchuk.testingservice.repository.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
}
