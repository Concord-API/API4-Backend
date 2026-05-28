package com.concord.trivio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseDTO {

    private Long id;
    private Long maintenanceId;
    private Long employeeId;
    private String employeeName;
    private String message;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
