package com.trainer_management_backend.trainer_management_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trainer_management_backend.trainer_management_backend.dto.AttendanceDTO;
import com.trainer_management_backend.trainer_management_backend.dto.EnrollmentDTO;
import com.trainer_management_backend.trainer_management_backend.model.Attendance;
import com.trainer_management_backend.trainer_management_backend.model.Batch;
import com.trainer_management_backend.trainer_management_backend.model.Trainee;
import com.trainer_management_backend.trainer_management_backend.repository.AttendanceRepository;
import com.trainer_management_backend.trainer_management_backend.repository.BatchRepository;
import com.trainer_management_backend.trainer_management_backend.repository.TraineeRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final BatchRepository batchRepository;
    private final AttendanceRepository attendanceRepository;
    
    public Trainee enrollInBatch(EnrollmentDTO dto) {
        Trainee trainee = traineeRepository.findById(dto.getTraineeId())
            .orElseThrow(() -> new RuntimeException("Trainee not found"));
        
        Batch batch = batchRepository.findById(dto.getBatchId())
            .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        trainee.setBatch(batch);
        return traineeRepository.save(trainee);
    }
    
    public Attendance markAttendance(AttendanceDTO dto) {
        Trainee trainee = traineeRepository.findById(dto.getTraineeId())
            .orElseThrow(() -> new RuntimeException("Trainee not found"));
        
        Batch batch = batchRepository.findById(dto.getBatchId())
            .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        // Check if attendance already exists for this date
        Optional<Attendance> existingAttendance = attendanceRepository
            .findByTraineeIdAndBatchIdAndDate(dto.getTraineeId(), dto.getBatchId(), dto.getDate());
        
        if (existingAttendance.isPresent()) {
            Attendance attendance = existingAttendance.get();
            attendance.setStatus(Attendance.AttendanceStatus.valueOf(dto.getStatus()));
            return attendanceRepository.save(attendance);
        }
        
        Attendance attendance = new Attendance();
        attendance.setTrainee(trainee);
        attendance.setBatch(batch);
        attendance.setDate(dto.getDate());
        attendance.setStatus(Attendance.AttendanceStatus.valueOf(dto.getStatus()));
        
        return attendanceRepository.save(attendance);
    }
}
