package com.ssms.shipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class WarehouseRequestDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NonNull
    @Positive
    private Double totalCapacity;
}
