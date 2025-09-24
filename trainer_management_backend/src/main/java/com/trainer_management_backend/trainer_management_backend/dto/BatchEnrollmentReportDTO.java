package com.trainer_management_backend.trainer_management_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchEnrollmentReportDTO {
    private Long batchId;
    private String course;
    private String location;
    private String trainerName;
    private int enrolledCount;
}