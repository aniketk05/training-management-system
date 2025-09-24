package com.trainer_management_backend.trainer_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trainer_management_backend.trainer_management_backend.model.TrainerAvailability;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainerAvailabilityRepository extends JpaRepository<TrainerAvailability, Long> {
    List<TrainerAvailability> findByTrainerId(Long trainerId);

    List<TrainerAvailability> findByTrainerIdAndAvailableDate(Long trainerId, LocalDate date);

    @Query("SELECT a FROM TrainerAvailability a WHERE a.trainer.id = :trainerId " +
            "AND a.availableDate BETWEEN :startDate AND :endDate")
    List<TrainerAvailability> findByTrainerIdAndDateRange(
            @Param("trainerId") Long trainerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}