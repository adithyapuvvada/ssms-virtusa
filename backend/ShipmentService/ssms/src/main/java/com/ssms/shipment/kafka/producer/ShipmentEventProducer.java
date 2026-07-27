package com.ssms.shipment.kafka.producer;

import com.ssms.common.event.ShipmentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentEventProducer {

    private final KafkaTemplate<String, ShipmentCompletedEvent> kafkaTemplate;

    public void sendShipmentCreatedEvent(ShipmentCompletedEvent event){
        kafkaTemplate.send("shipment-completed-event",event);
    }
}
