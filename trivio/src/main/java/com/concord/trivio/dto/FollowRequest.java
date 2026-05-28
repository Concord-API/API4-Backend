package com.concord.trivio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequest {

    private Long id;

    private Long maintenanceId;

    @NotBlank
    @Size(max = 1000)
    private String message;

    private Boolean active = true;
}
