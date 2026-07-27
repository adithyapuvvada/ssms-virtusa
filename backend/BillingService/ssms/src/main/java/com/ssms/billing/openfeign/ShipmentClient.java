package com.ssms.billing.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "SHIPMENT-SERVICE")
public interface ShipmentClient {

    @PutMapping("/ssms/shipment/shipments/dispatch/{shipmentId}")
    void dispatchShipment(@PathVariable Long shipmentId);
}
