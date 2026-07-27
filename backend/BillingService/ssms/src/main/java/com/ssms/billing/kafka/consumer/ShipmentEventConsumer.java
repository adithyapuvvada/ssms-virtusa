package com.ssms.billing.kafka.consumer;

import com.ssms.billing.dto.InventoryDTO;
import com.ssms.common.event.ShipmentCompletedEvent;
import com.ssms.billing.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentEventConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "shipment-completed-event",groupId = "billing-group")
    public void consumerShipmentEvent(ShipmentCompletedEvent event){
        System.out.println(event.toString());
        inventoryService.addInventory(new InventoryDTO(
                event.getItemName(),
                event.getShipmentCode(),
                event.getCompanyId(),
                event.getQuantity(),
                event.getUnitPrice(),
                event.getShipmentId()
        ));
    }
}
