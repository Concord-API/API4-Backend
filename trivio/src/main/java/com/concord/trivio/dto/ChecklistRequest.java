package com.concord.trivio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistRequest {

    @NotNull
    private Long maintenanceId;

    @NotBlank
    @Size(max = 255)
    private String description;

    private Boolean completed = false;
}
