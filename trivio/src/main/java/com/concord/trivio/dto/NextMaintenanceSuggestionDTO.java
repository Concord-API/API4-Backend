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
public class NextMaintenanceSuggestionDTO {
    private Long contractId;
    private LocalDate date;
    private MaintenanceType type;
    private MaintenanceStatus status;
    private Boolean preventive;
    private Double latitude;
    private Double longitude;
}
