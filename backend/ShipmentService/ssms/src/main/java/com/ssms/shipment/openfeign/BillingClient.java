package com.ssms.shipment.openfeign;

import com.ssms.shipment.dto.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "BILLING-SERVICE")
public interface BillingClient {

    @PostMapping("/ssms/billing/inventory/addInventory")
    void addInventory(InventoryDTO inventoryDTO);
}
