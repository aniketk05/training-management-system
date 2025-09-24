package com.trainer_management_backend.trainer_management_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceTrendDTO {
    private LocalDate date;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private double attendanceRate;
}