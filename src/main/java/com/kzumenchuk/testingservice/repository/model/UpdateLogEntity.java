package com.kzumenchuk.testingservice.repository.model;

import com.kzumenchuk.testingservice.util.EntityType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "update_log")
public class UpdateLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_log_id")
    private Long updateLogID;

    @Column(name = "entity_id")
    private Long entityID;

    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "operation_type")
    private String operationType;

    @Column(name = "updated_field")
    private String updatedField;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_user_id")
    private Long updateUserID;
}
