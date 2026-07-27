package com.ssms.shipment.util;

import com.ssms.shipment.dto.ShipmentRequestDTO;
import com.ssms.shipment.dto.ShipmentResponseDTO;
import com.ssms.shipment.dto.WarehouseDTO;
import com.ssms.shipment.entity.Shipment;
import com.ssms.shipment.entity.Warehouse;

public class ShipmentMapper {

    private ShipmentMapper(){}
    public static Shipment toEntity(ShipmentRequestDTO dto){
        return Shipment.builder()
                .companyId(dto.getCompanyId())
                .description(dto.getDescription())
                .volume(dto.getVolume())
                .build();
    }

    public static ShipmentResponseDTO toDTO(Shipment shipment){
        Warehouse warehouse = shipment.getWarehouse();

        WarehouseDTO warehouseDTO = WarehouseDTO.builder()
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .totalCapacity(warehouse.getTotalCapacity())
                .usedCapacity(warehouse.getUsedCapacity())
                .status(warehouse.getStatus())
                .build();

        return ShipmentResponseDTO.builder()
                .id(shipment.getId())
                .shipmentCode(shipment.getShipmentCode())
                .companyId(shipment.getCompanyId())
                .description(shipment.getDescription())
                .volume(shipment.getVolume())
                .arrivalDate(shipment.getArrivalDate())
                .dispatchDate(shipment.getDispatchDate())
                .status(shipment.getStatus())
                .warehouse(warehouseDTO)
                .build();
    }
}
