package com.trainer_management_backend.trainer_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trainer_management_backend.trainer_management_backend.model.Trainer;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    List<Trainer> findByLocation(String location);

    @Query("SELECT t FROM Trainer t WHERE t.location = :location AND " +
            "EXISTS (SELECT a FROM TrainerAvailability a WHERE a.trainer = t AND a.isBooked = false)")
    List<Trainer> findAvailableTrainersByLocation(@Param("location") String location);
}