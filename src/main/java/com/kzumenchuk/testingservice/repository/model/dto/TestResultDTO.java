package com.kzumenchuk.testingservice.repository.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResultDTO {
    private Long resultID;
    private Long userID;
    private Long testID;
    private int correctAnswersCount;
    private int correctAnswersPercentage;
    private LocalDateTime testingDate;
}
