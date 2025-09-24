package com.trainer_management_backend.trainer_management_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchProgressDTO {
    private Long batchId;
    private String course;
    private int totalTrainees;
    private Map<String, Integer> attendanceSummary;
    private List<TraineeInfoDTO> enrolledTrainees;
}