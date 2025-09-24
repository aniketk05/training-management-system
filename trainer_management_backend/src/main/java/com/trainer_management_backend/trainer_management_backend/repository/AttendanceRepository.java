package com.trainer_management_backend.trainer_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trainer_management_backend.trainer_management_backend.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByTraineeId(Long traineeId);
    List<Attendance> findByBatchId(Long batchId);
    Optional<Attendance> findByTraineeIdAndBatchIdAndDate(Long traineeId, Long batchId, LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.batch.id = :batchId AND a.date = :date")
    List<Attendance> findByBatchIdAndDate(@Param("batchId") Long batchId, @Param("date") LocalDate date);
}