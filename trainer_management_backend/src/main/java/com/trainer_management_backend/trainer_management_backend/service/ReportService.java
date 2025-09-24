package com.trainer_management_backend.trainer_management_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.trainer_management_backend.trainer_management_backend.dto.AttendanceTrendDTO;
import com.trainer_management_backend.trainer_management_backend.dto.BatchEnrollmentReportDTO;
import com.trainer_management_backend.trainer_management_backend.dto.TrainerAvailabilityReportDTO;
import com.trainer_management_backend.trainer_management_backend.model.Attendance;
import com.trainer_management_backend.trainer_management_backend.model.Batch;
import com.trainer_management_backend.trainer_management_backend.model.Trainer;
import com.trainer_management_backend.trainer_management_backend.model.TrainerAvailability;
import com.trainer_management_backend.trainer_management_backend.repository.AttendanceRepository;
import com.trainer_management_backend.trainer_management_backend.repository.BatchRepository;
import com.trainer_management_backend.trainer_management_backend.repository.TrainerAvailabilityRepository;
import com.trainer_management_backend.trainer_management_backend.repository.TrainerRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository availabilityRepository;
    private final BatchRepository batchRepository;
    private final AttendanceRepository attendanceRepository;
    
    public List<TrainerAvailabilityReportDTO> getTrainerAvailabilityReport() {
        List<Trainer> trainers = trainerRepository.findAll();
        
        return trainers.stream().map(trainer -> {
            List<TrainerAvailability> availabilities = availabilityRepository.findByTrainerId(trainer.getId());
            int totalAvailable = availabilities.size();
            int assigned = (int) availabilities.stream().filter(TrainerAvailability::isBooked).count();
            
            TrainerAvailabilityReportDTO report = new TrainerAvailabilityReportDTO();
            report.setTrainerId(trainer.getId());
            report.setTrainerName(trainer.getName());
            report.setTotalAvailableDays(totalAvailable);
            report.setAssignedDays(assigned);
            report.setOccupancyRate(totalAvailable > 0 ? (assigned * 100.0) / totalAvailable : 0);
            
            return report;
        }).collect(Collectors.toList());
    }
    
    public List<BatchEnrollmentReportDTO> getBatchEnrollmentReport() {
        List<Batch> batches = batchRepository.findAll();
        
        return batches.stream().map(batch -> {
            BatchEnrollmentReportDTO report = new BatchEnrollmentReportDTO();
            report.setBatchId(batch.getId());
            report.setCourse(batch.getCourse());
            report.setLocation(batch.getLocation());
            report.setTrainerName(batch.getTrainer().getName());
            report.setEnrolledCount(batch.getTrainees().size());
            
            return report;
        }).collect(Collectors.toList());
    }
    
    public List<AttendanceTrendDTO> getAttendanceTrends(Long batchId, LocalDate startDate, LocalDate endDate) {
        Batch batch = batchRepository.findById(batchId)
            .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        List<AttendanceTrendDTO> trends = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            List<Attendance> dayAttendance = attendanceRepository.findByBatchIdAndDate(batchId, currentDate);
            
            if (!dayAttendance.isEmpty()) {
                int present = 0, absent = 0, late = 0;
                
                for (Attendance attendance : dayAttendance) {
                    switch (attendance.getStatus()) {
                        case PRESENT -> present++;
                        case ABSENT -> absent++;
                        case LATE -> late++;
                    }
                }
                
                AttendanceTrendDTO trend = new AttendanceTrendDTO();
                trend.setDate(currentDate);
                trend.setPresentCount(present);
                trend.setAbsentCount(absent);
                trend.setLateCount(late);
                
                int total = present + absent + late;
                trend.setAttendanceRate(total > 0 ? ((present + late) * 100.0) / total : 0);
                
                trends.add(trend);
            }
            
            currentDate = currentDate.plusDays(1);
        }
        
        return trends;
    }
}