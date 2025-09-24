package com.trainer_management_backend.trainer_management_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "trainer_availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    @JsonBackReference("trainer-availability")
    private Trainer trainer;
    
    @Column(nullable = false)
    private LocalDate availableDate;
    
    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column(nullable = false)
    private LocalTime endTime;
    
    private boolean isBooked = false;
}