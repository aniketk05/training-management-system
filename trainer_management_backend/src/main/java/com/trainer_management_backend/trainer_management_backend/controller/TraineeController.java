package com.trainer_management_backend.trainer_management_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trainer_management_backend.trainer_management_backend.dto.AttendanceDTO;
import com.trainer_management_backend.trainer_management_backend.dto.EnrollmentDTO;
import com.trainer_management_backend.trainer_management_backend.model.Attendance;
import com.trainer_management_backend.trainer_management_backend.model.Trainee;
import com.trainer_management_backend.trainer_management_backend.repository.TraineeRepository;
import com.trainer_management_backend.trainer_management_backend.service.TraineeService;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeRepository traineeRepository;
    
    @GetMapping
    public ResponseEntity<List<Trainee>> getAllTrainees() {
        return ResponseEntity.ok(traineeRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Trainee> getTraineeById(@PathVariable Long id) {
        return traineeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Trainee> createTrainee(@RequestBody Trainee trainee) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(traineeRepository.save(trainee));
    }
    
    @PostMapping("/enroll")
    public ResponseEntity<Trainee> enrollInBatch(@RequestBody EnrollmentDTO dto) {
        return ResponseEntity.ok(traineeService.enrollInBatch(dto));
    }
    
    @PostMapping("/attendance")
    public ResponseEntity<Attendance> markAttendance(@RequestBody AttendanceDTO dto) {
        return ResponseEntity.ok(traineeService.markAttendance(dto));
    }
}
