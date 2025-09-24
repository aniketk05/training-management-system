package com.trainer_management_backend.trainer_management_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trainer_management_backend.trainer_management_backend.model.Batch;
import com.trainer_management_backend.trainer_management_backend.model.Trainer;
import com.trainer_management_backend.trainer_management_backend.repository.BatchRepository;
import com.trainer_management_backend.trainer_management_backend.repository.TrainerRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchController {
    private final BatchRepository batchRepository;
    private final TrainerRepository trainerRepository;
    
    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {
        return ResponseEntity.ok(batchRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(@PathVariable Long id) {
        return batchRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Batch> createBatch(@RequestBody Map<String, Object> batchData) {
        Batch batch = new Batch();
        batch.setCourse((String) batchData.get("course"));
        batch.setLocation((String) batchData.get("location"));
        
        Long trainerId = Long.valueOf(batchData.get("trainerId").toString());
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new RuntimeException("Trainer not found"));
        batch.setTrainer(trainer);
        
        if (batchData.containsKey("startDate")) {
            batch.setStartDate(java.time.LocalDate.parse(batchData.get("startDate").toString()));
        }
        if (batchData.containsKey("endDate")) {
            batch.setEndDate(java.time.LocalDate.parse(batchData.get("endDate").toString()));
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(batchRepository.save(batch));
    }
    
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Batch>> getBatchesByLocation(@PathVariable String location) {
        return ResponseEntity.ok(batchRepository.findByLocation(location));
    }
}