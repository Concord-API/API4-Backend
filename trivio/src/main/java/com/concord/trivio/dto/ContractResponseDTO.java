package com.concord.trivio.dto;

import java.time.LocalDate;
import java.util.List;

import com.concord.trivio.entity.Equipment;
import com.concord.trivio.entity.Requirement;
import com.concord.trivio.entity.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponseDTO {

    private Long id;
    private Client client;
    private LocalDate initialDate;
    private LocalDate finalDate;
    private Long recurrenceMaintenance;
    private Boolean active;
    private List<Equipment> equipments;
    private List<Requirement> requirements;

    private Double latitude;
    private Double longitude;
}
