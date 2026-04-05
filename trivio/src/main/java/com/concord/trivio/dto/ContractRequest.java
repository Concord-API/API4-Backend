package com.concord.trivio.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private LocalDate initialDate;

    @NotNull
    private LocalDate finalDate;

    @NotNull
    private Long recurrenceMaintenance;

    private Boolean active = true;

    private Set<Long> equipmentIds;

    private Set<Long> requirementIds;
}
