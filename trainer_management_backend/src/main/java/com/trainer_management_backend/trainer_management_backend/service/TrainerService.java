package com.trainer_management_backend.trainer_management_backend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trainer_management_backend.trainer_management_backend.dto.AvailabilityDTO;
import com.trainer_management_backend.trainer_management_backend.dto.BatchProgressDTO;
import com.trainer_management_backend.trainer_management_backend.dto.TraineeInfoDTO;
import com.trainer_management_backend.trainer_management_backend.model.Attendance;
import com.trainer_management_backend.trainer_management_backend.model.Batch;
import com.trainer_management_backend.trainer_management_backend.model.Trainer;
import com.trainer_management_backend.trainer_management_backend.model.TrainerAvailability;
import com.trainer_management_backend.trainer_management_backend.repository.BatchRepository;
import com.trainer_management_backend.trainer_management_backend.repository.TrainerAvailabilityRepository;
import com.trainer_management_backend.trainer_management_backend.repository.TrainerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository availabilityRepository;
    private final BatchRepository batchRepository;
    
    public TrainerAvailability setAvailability(AvailabilityDTO dto) {
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
            .orElseThrow(() -> new RuntimeException("Trainer not found"));
        
        TrainerAvailability availability = new TrainerAvailability();
        availability.setTrainer(trainer);
        availability.setAvailableDate(dto.getAvailableDate());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setBooked(false);
        
        return availabilityRepository.save(availability);
    }
    
    public List<TrainerAvailability> getTrainerAvailability(Long trainerId) {
        return availabilityRepository.findByTrainerId(trainerId);
    }
    
    public BatchProgressDTO getBatchProgress(Long trainerId, Long batchId) {
        Batch batch = batchRepository.findById(batchId)
            .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        if (!batch.getTrainer().getId().equals(trainerId)) {
            throw new RuntimeException("Trainer not assigned to this batch");
        }
        
        BatchProgressDTO progressDTO = new BatchProgressDTO();
        progressDTO.setBatchId(batchId);
        progressDTO.setCourse(batch.getCourse());
        progressDTO.setTotalTrainees(batch.getTrainees().size());
        
        // Calculate attendance summary
        int presentCount = 0, absentCount = 0, lateCount = 0;
        for (Attendance attendance : batch.getAttendances()) {
            switch (attendance.getStatus()) {
                case PRESENT -> presentCount++;
                case ABSENT -> absentCount++;
                case LATE -> lateCount++;
            }
        }
        
        progressDTO.setAttendanceSummary(java.util.Map.of(
            "present", presentCount,
            "absent", absentCount,
            "late", lateCount
        ));
        
        // Get enrolled trainees info
        List<TraineeInfoDTO> traineeInfos = batch.getTrainees().stream()
            .map(trainee -> {
                TraineeInfoDTO info = new TraineeInfoDTO();
                info.setId(trainee.getId());
                info.setName(trainee.getName());
                
                long presentDays = trainee.getAttendances().stream()
                    .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                    .count();
                long totalDays = trainee.getAttendances().size();
                
                double percentage = totalDays > 0 ? (presentDays * 100.0) / totalDays : 0;
                info.setAttendancePercentage(percentage);
                
                return info;
            })
            .collect(Collectors.toList());
        
        progressDTO.setEnrolledTrainees(traineeInfos);
        
        return progressDTO;
    }
    
    public List<Trainer> getAvailableTrainersByLocation(String location) {
        return trainerRepository.findAvailableTrainersByLocation(location);
    }
}