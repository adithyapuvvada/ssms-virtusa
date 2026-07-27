package com.ssms.shipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ShipmentRequestDTO {

    @NonNull
    private Long companyId;

    @NotBlank
    private String description;

    @NonNull
    @Positive
    private Integer volume;

}
