package com.trainer_management_backend.trainer_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainer_management_backend.trainer_management_backend.model.Trainee;

import java.util.List;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    List<Trainee> findByBatchId(Long batchId);
}
