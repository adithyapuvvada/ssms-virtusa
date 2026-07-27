package com.ssms.shipment.dto;

import com.ssms.shipment.entity.WarehouseStatus;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@Builder
public class WarehouseDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NonNull
    @Positive
    private Double totalCapacity;
    private Double usedCapacity;
    private WarehouseStatus status;

}
