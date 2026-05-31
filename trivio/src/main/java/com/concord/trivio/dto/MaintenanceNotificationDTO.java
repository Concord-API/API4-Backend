package com.concord.trivio.dto;

import java.time.LocalDate;

import com.concord.trivio.entity.MaintenanceStatus;
import com.concord.trivio.entity.MaintenanceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceNotificationDTO {

    private Long maintenanceId;
    private Long contractId;
    private String clientName;
    private LocalDate maintenanceDate;
    private Integer daysUntilMaintenance;
    private MaintenanceType type;
    private MaintenanceStatus status;
}