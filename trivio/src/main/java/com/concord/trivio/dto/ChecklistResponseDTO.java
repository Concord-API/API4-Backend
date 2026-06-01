package com.concord.trivio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistResponseDTO {

    private Long id;
    private Long maintenanceId;
    private String description;
    private Boolean completed;
}
