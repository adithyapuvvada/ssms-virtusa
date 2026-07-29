package com.ssms.shipment.controller;


import com.ssms.shipment.dto.ShipmentRequestDTO;
import com.ssms.shipment.dto.ShipmentResponseDTO;
import com.ssms.shipment.entity.Shipment;
import com.ssms.shipment.exception.ResourceNotFoundException;
import com.ssms.shipment.service.ShipmentService;
import com.ssms.shipment.util.ShipmentMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssms/shipment/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping
    public List<ShipmentResponseDTO> getAllShipments(@RequestHeader(value="X-User-Role", required=false) String role,Long companyId){
        if(!isAuthorized(role,"ROLE_MANAGER", "ROLE_SUPPLIER","ROLE_INVENTORY_MANAGER","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        List<Shipment> shipments = shipmentService.getAllShipments(role,companyId);
        return  shipments.stream().map(ShipmentMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ShipmentResponseDTO getShipmentById(@RequestHeader(value="X-User-Role", required=false) String role,
                                               @PathVariable Long id) {
        if(!isAuthorized(role, "ROLE_MANAGER", "ROLE_SUPPLIER", "ROLE_INVENTORY_MANAGER", "ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return ShipmentMapper.toDTO(shipmentService.findById(id));
    }


    @PostMapping
    public ShipmentResponseDTO addShipment(@RequestHeader(value="X-User-Role", required=false) String role,
                                           @Valid @RequestBody ShipmentRequestDTO requestDTO){
        if(!isAuthorized(role,"ROLE_MANAGER", "ROLE_SUPPLIER", "ROLE_INVENTORY_MANAGER","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }

        Shipment shipment = ShipmentMapper.toEntity(requestDTO);
        Shipment savedShipment = shipmentService.addIncomingShipment(shipment,role);
        //calling billing
        shipmentService.addInventory(savedShipment);
        return ShipmentMapper.toDTO(savedShipment);
    }

    private boolean isAuthorized(String userRole, String... allowedRoles) {
        if (userRole == null) return false;
        for (String allowed : allowedRoles) {
            if (allowed.contains(userRole)) return true;
        }
        return false;
    }

}
