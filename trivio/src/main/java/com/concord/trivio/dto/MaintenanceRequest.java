package com.concord.trivio.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.concord.trivio.entity.MaintenanceStatus;
import com.concord.trivio.entity.MaintenanceType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequest {

    private Long contractId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Boolean preventive;

    @NotNull
    private MaintenanceType type;

    @NotNull
    private MaintenanceStatus status;

    private Boolean active = true;

    private Set<Long> employeeIds;

    private Double latitude;

    private Double longitude;

    private LocalTime startTime;

    private LocalTime endTime;
}
