package com.ssms.shipment.controller;

import com.ssms.shipment.dto.WarehouseDTO;
import com.ssms.shipment.dto.WarehouseRequestDTO;
import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.exception.ResourceNotFoundException;
import com.ssms.shipment.service.WarehouseService;
import com.ssms.shipment.util.WarehouseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssms/shipment/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    public WarehouseDTO createWarehouse(@RequestHeader(value="X-User-Role", required=false) String role,
                                        @Valid @RequestBody WarehouseRequestDTO warehouseRequestDTO){

        if(!isAuthorized(role,"ROLE_MANAGER","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }

        Warehouse warehouse = WarehouseMapper.toEntity(warehouseRequestDTO);
        Warehouse savedWarehouse = warehouseService.createWarehouse(warehouse);
        return WarehouseMapper.toDTO(savedWarehouse);
    }



    @GetMapping
    public List<WarehouseDTO> getAllWarehouses(@RequestHeader(value="X-User-Role", required=false) String role){
        if(!isAuthorized(role,"ROLE_MANAGER","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return warehouseService.getAllWarehouses().stream().map(WarehouseMapper::toDTO).toList();
    }

    // Simple helper method to keep the code clean
    private boolean isAuthorized(String userRole, String... allowedRoles) {
        if (userRole == null) return false;
        for (String allowed : allowedRoles) {
            if (allowed.contains(userRole)) return true;
        }
        return false;
    }
}


