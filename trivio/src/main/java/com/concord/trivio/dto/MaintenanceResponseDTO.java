package com.concord.trivio.dto;

import java.time.LocalDate;
import java.util.List;

import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.Employee;
import com.concord.trivio.entity.MaintenanceStatus;
import com.concord.trivio.entity.MaintenanceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponseDTO {

    private Long id;
    private Contract contract;
    private LocalDate date;
    private Boolean preventive;
    private MaintenanceType type;
    private MaintenanceStatus status;
    private Boolean active;
    private List<Employee> employees;

    private Double latitude;

    private Double longitude;
}
