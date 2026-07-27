package com.ssms.billing.controller;


import com.ssms.billing.entity.Inventory;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssms/billing/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/all")
    public List<Inventory> getAllInventory(@RequestHeader(value="X-User-Role", required=false) String role,Long companyId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_MANAGER","ROLE_ADMIN","ROLE_INVENTORY_MANAGER")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return inventoryService.getAllInventory(role,companyId);
    }

    @GetMapping("/shipment/{shipmentId}")
    public Inventory getByShipmentId(@RequestHeader(value="X-User-Role", required=false) String role,
                                     @PathVariable("shipmentId") Long shipmentId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_MANAGER","ROLE_ADMIN","ROLE_INVENTORY_MANAGER")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return inventoryService.getByShipmentId(shipmentId);
    }

    private boolean isAuthorized(String userRole, String... allowedRoles) {
        for (String allowed : allowedRoles) {
            if (allowed.contains(userRole)) return true;
        }
        return false;
    }
}
