package com.ssms.shipment.dto;

import com.ssms.shipment.entity.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentResponseDTO {
    private Long id;
    private String shipmentCode;
    private Long companyId;
    private String description;
    private Integer volume;
    private LocalDateTime arrivalDate;
    private LocalDateTime dispatchDate;
    private ShipmentStatus status;
    private WarehouseDTO warehouse;
}
