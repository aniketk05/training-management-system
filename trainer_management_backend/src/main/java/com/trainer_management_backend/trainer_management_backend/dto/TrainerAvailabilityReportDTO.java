package com.trainer_management_backend.trainer_management_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerAvailabilityReportDTO {
    private Long trainerId;
    private String trainerName;
    private int totalAvailableDays;
    private int assignedDays;
    private double occupancyRate;
}