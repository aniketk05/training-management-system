package com.trainer_management_backend.trainer_management_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trainer_management_backend.trainer_management_backend.dto.AttendanceTrendDTO;
import com.trainer_management_backend.trainer_management_backend.dto.BatchEnrollmentReportDTO;
import com.trainer_management_backend.trainer_management_backend.dto.TrainerAvailabilityReportDTO;
import com.trainer_management_backend.trainer_management_backend.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    
    @GetMapping("/trainer-availability")
    public ResponseEntity<List<TrainerAvailabilityReportDTO>> getTrainerAvailabilityReport() {
        return ResponseEntity.ok(reportService.getTrainerAvailabilityReport());
    }
    
    @GetMapping("/batch-enrollments")
    public ResponseEntity<List<BatchEnrollmentReportDTO>> getBatchEnrollmentReport() {
        return ResponseEntity.ok(reportService.getBatchEnrollmentReport());
    }
    
    @GetMapping("/attendance-trends")
    public ResponseEntity<List<AttendanceTrendDTO>> getAttendanceTrends(
            @RequestParam Long batchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getAttendanceTrends(batchId, startDate, endDate));
    }
}
