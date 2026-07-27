package com.ssms.shipment.util;

import com.ssms.shipment.dto.WarehouseDTO;
import com.ssms.shipment.dto.WarehouseRequestDTO;
import com.ssms.shipment.entity.Warehouse;

public class WarehouseMapper {

    private WarehouseMapper() {
        // prevent instantiation
    }

    public static WarehouseDTO toDTO(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }

        return WarehouseDTO.builder()
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .totalCapacity(warehouse.getTotalCapacity())
                .usedCapacity(warehouse.getUsedCapacity())
                .status(warehouse.getStatus())
                .build();
    }

    public static Warehouse toEntity(WarehouseRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Warehouse.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .totalCapacity(dto.getTotalCapacity())
                .build();
    }
}