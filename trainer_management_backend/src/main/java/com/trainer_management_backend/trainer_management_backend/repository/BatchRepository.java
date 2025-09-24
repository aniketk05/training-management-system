package com.trainer_management_backend.trainer_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trainer_management_backend.trainer_management_backend.model.Batch;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByTrainerId(Long trainerId);
    List<Batch> findByLocation(String location);
    
    @Query("SELECT b FROM Batch b WHERE b.trainer.id = :trainerId")
    List<Batch> findBatchesByTrainerId(Long trainerId);
}