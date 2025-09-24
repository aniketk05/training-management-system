package com.trainer_management_backend.trainer_management_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trainer_management_backend.trainer_management_backend.dto.AvailabilityDTO;
import com.trainer_management_backend.trainer_management_backend.dto.BatchProgressDTO;
import com.trainer_management_backend.trainer_management_backend.model.Trainer;
import com.trainer_management_backend.trainer_management_backend.model.TrainerAvailability;
import com.trainer_management_backend.trainer_management_backend.repository.TrainerRepository;
import com.trainer_management_backend.trainer_management_backend.service.TrainerService;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerRepository trainerRepository;
    
    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return ResponseEntity.ok(trainerRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable Long id) {
        return trainerRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Trainer> createTrainer(@RequestBody Trainer trainer) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(trainerRepository.save(trainer));
    }
    
    @PostMapping("/{id}/availability")
    public ResponseEntity<TrainerAvailability> setAvailability(@RequestBody AvailabilityDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(trainerService.setAvailability(dto));
    }
    
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<TrainerAvailability>> getAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getTrainerAvailability(id));
    }
    
    @GetMapping("/{trainerId}/batches/{batchId}/progress")
    public ResponseEntity<BatchProgressDTO> getBatchProgress(
            @PathVariable Long trainerId,
            @PathVariable Long batchId) {
        return ResponseEntity.ok(trainerService.getBatchProgress(trainerId, batchId));
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Trainer>> getAvailableTrainersByLocation(
            @RequestParam String location) {
        return ResponseEntity.ok(trainerService.getAvailableTrainersByLocation(location));
    }
}
